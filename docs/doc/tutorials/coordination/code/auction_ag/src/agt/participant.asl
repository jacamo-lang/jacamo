+auction(service, D)[source(A)]  <- .broadcast(tell,bid(D, math.random * 100 + 10)).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
