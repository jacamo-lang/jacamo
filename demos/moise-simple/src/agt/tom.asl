!adopt_role(r3,grp1).

+!adopt_role(R,G)
   <- .wait(500);
      lookupArtifact(G,A);
      focus(A);
      adoptRole(R);
   .

+!c <- .print("doing goal c");  .wait(1000).
+!e <- .print("doing goal e");  .wait(1000).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
{ include("$moiseJar/asl/org-obedient.asl") }
