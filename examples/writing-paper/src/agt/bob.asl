{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
{ include("$moise/asl/org-obedient.asl") }

/* application domain goals */
+!wtitle     <- .wait(500); .print("writing title...").
+!wabs       <- .print("writing abstract...").
+!wsectitles <- .print("writing section title...").
+!wconc      <- .print("writing conclusion...").
+!wp         <- .print("paper finished!").

/* other plans */

// when a scheme is created for writing papers (goal wp is waiting), I commit to mManager
+goalState(_,wp,_,_,waiting)[workspace(_,W)] <- commitMission(mManager)[wid(W)].

// signals
+normFailure(N)  <- .print("norm failure event: ", N).
+destroyed(Art)  <- .print("Artifact ",Art," destroyed").
