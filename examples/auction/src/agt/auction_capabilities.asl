
+!do_auction(Id,P)
   <- // creates a scheme to coordinate the auction
      .concat("sch_",Id,SchName);
      ?joined(aorg,OrgWks); // get the workspace id of the org
      createScheme(SchName, doAuction, SchArtId)[wid(OrgWks)];
      setArgumentValue(auction,"Id",Id)[artifact_id(SchArtId)];
      setArgumentValue(auction,"Service",P)[artifact_id(SchArtId)];
      //debug(inspector_gui(on))[artifact_id(SchArtId)];
      .my_name(Me); setOwner(Me)[artifact_id(SchArtId)];  // I am the owner of this scheme!
      focus(SchArtId);
      addScheme(SchName)[wid(OrgWks)];  // set the group as responsible for the scheme
      commitMission(mAuctioneer)[artifact_id(SchArtId)].

+?joined(Name,Id) <- .print("waiting for workspace ",Name); .wait(100); ?joined(Name,Id).

/* plans for organisational goals */

+!start[scheme(Sch)]                        // plan for the goal start defined in the scheme
   <- ?goalArgument(Sch,auction,"Id",Id);   // retrieve auction Id and service description S
      ?goalArgument(Sch,auction,"Service",S);
      .print("Start scheme ",Sch," for ",S);
      makeArtifact(Id, "auction_env.AuctionArtifact", [], ArtId); // create the auction artifact
      focus(ArtId);
      start(S)[artifact_id(ArtId)].

+!decide[scheme(Sch)]
   <- ?goalArgument(Sch,auction,"Id",Id);
      ?focusing(ArtId,Id,_,_,_,_);
      stop[artifact_id(ArtId)].

+!bid[scheme(Sch)]
   <- ?goalArgument(Sch,auction,"Id",Id);
      lookupArtifact(Id,AId);
      focus(AId); //focusWhenAvailable(Id);
      if (math.random  < 0.9) {              // bid in 80% of the cases
        .wait(math.random * 2000 + 500);     // to simulate some "decision" reasoning
        bid(math.random * 100 + 10)[artifact_id(AId)];
      } else {
        .print("The following error in console is normal, it is due to the fail while bidding.")
        .fail;                               // fail otherwise
      }.

+winner(W) : .my_name(W) <- .print("I Won!").

+winner(W)[artifact_id(AId)]
   : W \== no_winner &
    .my_name(Me) & play(Me,auctioneer,_)  // announce if I am the auctioneer
   <- ?task(S)[artifact_id(AId)];
      ?best_bid(V)[artifact_id(AId)];
      .print("Winner for ", S, " is ",W," with ", V).

+oblUnfulfilled( obligation(Ag,_,done(Sch,bid,Ag),_ ) )[artifact_id(AId)]  // it is the case that a bid was not achieved
   : .my_name(Me) & play(Me,auctioneer,_)  // handle unfulfilled obl if I am the auctioneer
   <- .print("Participant ",Ag," didn't bid on time! S/he will be placed in a block list");
       // TODO: implement a block list artifact
       admCommand("goalSatisfied(bid)")[artifact_id(AId)]. // go on in the scheme....

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
