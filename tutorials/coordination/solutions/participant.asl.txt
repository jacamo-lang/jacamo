+auction(service, D)[source(A)]  <- .broadcast(tell,bid(D, math.random * 100 + 10)).

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
{ include("$moise/asl/org-obedient.asl") }
