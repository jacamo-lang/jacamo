+!print_h    : focused(jacamo,gui,ArtId) <- printChar(0,1,"H")[artifact_id(ArtId)]; .wait(700).
+!print_e    : focused(jacamo,gui,ArtId) <- printChar(0,2,"e")[artifact_id(ArtId)]; .wait(700).
+!print_l1   : focused(jacamo,gui,ArtId) <- printChar(0,3,"l")[artifact_id(ArtId)]; .wait(700).
+!print_l2   : focused(jacamo,gui,ArtId) <- printChar(0,4,"l")[artifact_id(ArtId)]; .wait(700).
+!print_o1   : focused(jacamo,gui,ArtId) <- printChar(0,5,"o")[artifact_id(ArtId)]; .wait(700).
+!print_spc  : focused(jacamo,gui,ArtId) <- printChar(0,6," ")[artifact_id(ArtId)]; .wait(700).
+!print_w    : focused(jacamo,gui,ArtId) <- printChar(1,1,"W")[artifact_id(ArtId)]; .wait(700).
+!print_o2   : focused(jacamo,gui,ArtId) <- printChar(1,2,"o")[artifact_id(ArtId)]; .wait(700).
+!print_r    : focused(jacamo,gui,ArtId) <- printChar(1,3,"r")[artifact_id(ArtId)]; .wait(700).
+!print_l3   : focused(jacamo,gui,ArtId) <- printChar(1,4,"l")[artifact_id(ArtId)]; .wait(700).
+!print_d    : focused(jacamo,gui,ArtId) <- printChar(1,5,"d")[artifact_id(ArtId)]; .wait(700).
+!print_excl : focused(jacamo,gui,ArtId) <- printChar(1,6,"!")[artifact_id(ArtId)]; .wait(700).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
