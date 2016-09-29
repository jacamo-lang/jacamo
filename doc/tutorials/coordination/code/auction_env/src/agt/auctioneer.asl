+!start(Id,P) 
   <- makeArtifact(Id, "auction_env.AuctionArtifact", [], ArtId);
      .print("Auction artifact created for ",P);
      focus(ArtId);
      .broadcast(achieve,focus(Id));  // ask all others to focus on this new artifact
      start(P)[artifact_id(ArtId)];
      .at("now + 5 seconds", {+!decide(Id)}).
      
+!decide(Id)
   <- stop[artifact_name(Id)].
   
+winner(W)[artifact_id(AId)] : W \== no_winner
   <- ?task(S)[artifact_id(AId)];
      ?best_bid(V)[artifact_id(AId)];
      .print("Winner for ", S, " is ",W," with ", V).         

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
