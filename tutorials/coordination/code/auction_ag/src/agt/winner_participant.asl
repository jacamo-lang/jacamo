+bid(S,P)  <- .wait(1000); .broadcast(tell,bid(S, P*0.9)); .print("Bid ",P*0.9, " for ",S).

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
{ include("$moise/asl/org-obedient.asl") }
