!start.

+!start <- .send(alice,tell,hello).

+hello[source(A)]
   <- .print("Hello form ", A).

{ include("$jacamoJar/templates/common-cartago.asl") }
