!r(20).

+!r(0).
+!r(X) : X=10 <- +b(X); ia.snapshot("sn.ser"); !r(X-1).
+!r(X)  <- .wait(100); ?count(C); .print("my step: ",X,", counter: ",C); !r(X-1).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
