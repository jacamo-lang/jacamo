{ include("$jasonJar/test/jason/inc/test_assert.asl") }

!test.

/* Plans */
+!test
   <- !start;
      !stopF;
      !createArt("/main/w1");
      !quit;
      !enter_again;
   .

+!start : true
   <- .print("hello world.");
      createWorkspace("/main/w1");
      createWorkspace("/main/w2");
      joinWorkspace("/main/w1",W1a);
      joinWorkspace("/main/w2",W2a);

      !assert_true(joinedWsp(_,w1,"/main/w1"));
      !assert_true(joinedWsp(_,w2,"/main/w2"));

      makeArtifact(c1,"tools.Counter",[10],ArtId);
      focus(ArtId);
      !assert_true(focusing(_,c1,"tools.Counter",_,w2,"/main/w2"));

      !assert_true(count(10));
   .

+!stopF : focusing(ArtId,c1,_,_,_,_)
   <- stopFocus(ArtId);
      !assert_false(count(_));
      .wait(100);
      !assert_false(joinedWsp(_,w2,"/main/w2"));
   .

-focusing(_,_,_,WId,_,_)
   : not focusing(_,_,_,WId,_,_) &
     joinedWsp(WId,_,_)
   <- quitWorkspace(WId).

+!createArt(W)
   <- joinWorkspace(W,W1a);
      makeArtifact(cw1,"tools.Counter",[20],ArtId);
      focus(ArtId);
      !assert_true(count(20));
   .

+!quit <-
      !assert_true(joinedWsp(_,w1,"/main/w1"));
      ?joinedWsp(A1,_,"/main/w1");
      //?joinedWsp(A2,_,"/main/w2");
      quitWorkspace(A1);
      //quitWorkspace(A2); // should quit by stopF
      !assert_false(joinedWsp(_,w1,"/main/w1"));
      !assert_false(count(_));
      .print("Done quit");
   .

+!enter_again
    <- joinWorkspace("/main/w1",W1a);
       !assert_true(joinedWsp(_,w1,"/main/w1"));
       lookupArtifact(cw1,ArtId);
       focus(ArtId)[wid(W1a)];
       !assert_true(count(20));
    .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
