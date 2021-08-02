+hello[source(A)]
   <- .print("Hello form ", A);
      .send(A,tell,hello).

+count(X) <- .print("Counter is ",X).

{ include("$jacamoJar/templates/common-cartago.asl") }
