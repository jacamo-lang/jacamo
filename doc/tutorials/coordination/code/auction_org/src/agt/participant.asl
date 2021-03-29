+!bid[scheme(Sch)]
   <- ?goalArgument(Sch,auction,"Id",Id);    // retrieve auction id and focus on the artifact
      lookupArtifact(Id,AId);
      focus(AId);
      if (math.random  < 0.5) {              // bid in 50% of the cases
        .wait(math.random * 2000 + 500);     // to simulate some "decision" reasoning
        bid(math.random * 100 + 10)[artifact_id(AId)];
      } else {
        .fail;                               // fail otherwise
      }.

-!bid[error(ia_failed)] <- .print("I didn't bid!").
-!bid[error_msg(M)]     <- .print("Error bidding: ",M).

+winner(W) : .my_name(W) <- .print("I Won!").

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
