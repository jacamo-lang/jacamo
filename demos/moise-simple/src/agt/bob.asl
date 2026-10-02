!adopt_role(r1,grp1).

+!adopt_role(R,G)
   <- .wait(500);
   lookupArtifact(G,A);
      focus(A);
      adoptRole(R);
   .

+!a <- .print("doing goal a");  .wait(1000).
+!f <- .print("doing goal f");  .wait(1000).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
{ include("$moiseJar/asl/org-obedient.asl") }
