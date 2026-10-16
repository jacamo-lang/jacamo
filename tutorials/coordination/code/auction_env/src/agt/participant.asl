+!focus(A) // goal sent by the auctioneer
   <- lookupArtifact(A,ToolId);
      focus(ToolId).

+task(D)[artifact_id(AId)] : running("yes")[artifact_id(AId)]
   <- bid(math.random * 100 + 10)[artifact_id(AId)].

+winner(W) : .my_name(W) <- .print("I Won!").

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
{ include("$moise/asl/org-obedient.asl") }
