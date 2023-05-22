+!start(Id,P)
   <- makeArtifact(Id, "auction_env.AuctionArtifact", [], ArtId);
      .print("Auction artifact created for ",P);
      Id::focus(ArtId);  // place observable properties of this auction in a particular name space
      Id::start(P);
      .broadcast(achieve,focus(Id));  // ask all others to focus on this new artifact
      //.at("now + 5 seconds", {+!decide(Id)}).
      !decide(Id).

//+!decide(Id)
//   <- Id::stop.

// update the time when the last bid was perceived
+NS::best_bid(V)
   <- -+NS::last_bid_time(system.time).

// stops the auction if we have 5secconds of "silence" in the room
+!decide(Id)
    : Id::last_bid_time(L) & system.time - L > 5000
   <- Id::stop.
+!decide(Id)
  <- .wait(500); !decide(Id).

+NS::winner(W) : W \== no_winner
   <- ?NS::task(S);
      ?NS::best_bid(V);
      .print("Winner for ", S, " is ",W," with ", V).

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
{ include("$moise/asl/org-obedient.asl") }
