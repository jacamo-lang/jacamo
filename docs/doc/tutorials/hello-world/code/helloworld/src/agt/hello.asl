+!print_h    : focused(jacamo,gui,ArtId) <- printMsg("H")[artifact_id(ArtId)]; .wait(700).
+!print_e    : focused(jacamo,gui,ArtId) <- printMsg("e")[artifact_id(ArtId)]; .wait(700).
+!print_l1   : focused(jacamo,gui,ArtId) <- printMsg("l")[artifact_id(ArtId)]; .wait(700).
+!print_l2   : focused(jacamo,gui,ArtId) <- printMsg("l")[artifact_id(ArtId)]; .wait(700).
+!print_l3   : focused(jacamo,gui,ArtId) <- printMsg("l")[artifact_id(ArtId)]; .wait(700).
+!print_spc  : focused(jacamo,gui,ArtId) <- printMsg(" ")[artifact_id(ArtId)]; .wait(700).
+!print_w    : focused(jacamo,gui,ArtId) <- printMsg("W")[artifact_id(ArtId)]; .wait(700).
+!print_o1   : focused(jacamo,gui,ArtId) <- printMsg("o")[artifact_id(ArtId)]; .wait(700).
+!print_o2   : focused(jacamo,gui,ArtId) <- printMsg("o")[artifact_id(ArtId)]; .wait(700).
+!print_r    : focused(jacamo,gui,ArtId) <- printMsg("r")[artifact_id(ArtId)]; .wait(700).
+!print_d    : focused(jacamo,gui,ArtId) <- printMsg("d")[artifact_id(ArtId)]; .wait(700).
+!print_excl : focused(jacamo,gui,ArtId) <- printMsg("!")[artifact_id(ArtId)]; .wait(700).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
