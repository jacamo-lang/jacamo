
+!do_auction(Id,P) 
   <- // creates a scheme to coordinate the auction
      .concat("sch_",Id,SchName);
      makeArtifact(SchName, "ora4mas.nopl.SchemeBoard",["src/org/auction-os.xml", doAuction, false, true ],SchArtId);
	  setArgumentValue(auction,"Id",Id)[artifact_id(SchArtId)]; 
	  setArgumentValue(auction,"Service",P)[artifact_id(SchArtId)]; 
      .my_name(Me); setOwner(Me)[artifact_id(SchArtId)];  // I am the owner of this scheme!
      focus(SchArtId);
      addScheme(SchName);  // set the group as responsible for the scheme
	  commitMission(mAuctioneer)[artifact_id(SchArtId)].

/* plans for organizational goals */

+!start[scheme(Sch)]                        // plan for the goal start defined in the scheme 
   <- ?goalState(Sch, auction(Id,S),_,_,_); // retrieve auction Id and service description S 
      .print("Start scheme ",Sch," for ",S);
      makeArtifact(Id, "auction_env.AuctionArtifact", [], ArtId); // create the auction artifact
      focus(ArtId);
      start(S)[artifact_id(ArtId)];
      .
      
+!decide[scheme(Sch)] 
   <- ?goalState(Sch, auction(Id,S),_,_,_); // retrieve the auction artifact name Id
      stop[artifact_name(Id)].
            
+!bid[scheme(Sch)] 
   <- ?goalState(Sch, auction(Id,S),_,_,_); // retrieve auction id and focus on the artifact
      lookupArtifact(Id,AId);
      focus(AId);
      if (math.random  < 0.8) {              // bid in 80% of the cases 
      	.wait(math.random * 2000 + 500);     // to simulate some "decision" reasoning 
      	bid(math.random * 100 + 10)[artifact_id(AId)];
      } else {
      	.fail;                               // fail otherwise
      }.

+winner(W) : .my_name(W) <- .print("I Won!").
            
+winner(W)[artifact_id(AId)]
   : W \== no_winner &
    .my_name(Me) & play(Me,auctioneer,_)  // announce if I am the auctioneer
   <- ?task(S)[artifact_id(AId)];
      ?best_bid(V)[artifact_id(AId)];
      .print("Winner for ", S, " is ",W," with ", V).         

+oblUnfulfilled( obligation(Ag,_,achieved(Sch,bid,Ag),_ ) )[artifact_id(AId)]  // it is the case that a bid was not achieved
   : .my_name(Me) & play(Me,auctioneer,_)  // handle unfulfilled obl if I am the auctioneer
   <- .print("Participant ",Ag," didn't bid on time! S/he will be placed in a blacklist");
       // TODO: implement an black list artifact
       .concat("goalAchieved(",Ag,",",bid,")",Cmd);         // create an administrative command like "goalAchieved(alice,bid)", to be send to the scheme
       admCommand(Cmd)[artifact_id(AId)].
   
{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
