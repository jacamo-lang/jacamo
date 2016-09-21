// Agent hello in project hello world

/* Initial beliefs and rules */

/* Initial goals */

//!start.

/* Plans */

+!print_h    <- ?jcm::focused(jacamo,gui,ArtId); printMsg("H")[artifact_id(ArtId)]; .wait(700).
+!print_e    <- ?jcm::focused(jacamo,gui,ArtId); printMsg("e")[artifact_id(ArtId)]; .wait(700).
+!print_l1   <- ?jcm::focused(jacamo,gui,ArtId); printMsg("l")[artifact_id(ArtId)]; .wait(700).
+!print_l2   <- ?jcm::focused(jacamo,gui,ArtId); printMsg("l")[artifact_id(ArtId)]; .wait(700).
+!print_l3   <- ?jcm::focused(jacamo,gui,ArtId); printMsg("l")[artifact_id(ArtId)]; .wait(700).
+!print_spc  <- ?jcm::focused(jacamo,gui,ArtId); printMsg(" ")[artifact_id(ArtId)]; .wait(700).
+!print_w    <- ?jcm::focused(jacamo,gui,ArtId); printMsg("W")[artifact_id(ArtId)]; .wait(700).
+!print_o1   <- ?jcm::focused(jacamo,gui,ArtId); printMsg("o")[artifact_id(ArtId)]; .wait(700).
+!print_o2   <- ?jcm::focused(jacamo,gui,ArtId); printMsg("o")[artifact_id(ArtId)]; .wait(700).
+!print_r    <- ?jcm::focused(jacamo,gui,ArtId); printMsg("r")[artifact_id(ArtId)]; .wait(700).
+!print_d    <- ?jcm::focused(jacamo,gui,ArtId); printMsg("d")[artifact_id(ArtId)]; .wait(700).
+!print_excl <- ?jcm::focused(jacamo,gui,ArtId); printMsg("!")[artifact_id(ArtId)]; .wait(700).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have a agent that always complies with its organization
{ include("$jacamoJar/templates/org-obedient.asl") }
