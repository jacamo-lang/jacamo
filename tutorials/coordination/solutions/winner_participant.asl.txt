+bid(S,P)  <- .wait(1000); .broadcast(tell,bid(S, P*0.9)); .print("Bid ",P*0.9, " for ",S).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
