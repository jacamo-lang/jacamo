!start.

+!start : true
    <- makeArtifact(nb1,"ora4mas.nopl.NormativeBoard",[],AId);
       focus(AId);
       debug(inspector_gui(on));
       load("src/org/demo.npl");
       addFact(vl(20)); // triggers the norm n1
       addFact(c(10));  // add condition for the application of sanction s1
       makeArtifact(ps1,"police.Sanctioner",[],SArt);
       listen(AId)[artifact_id(SArt)]; // ps1 get normative events (including sanctions) from nb1
       .wait(4000);
       removeFact(vl(20)); // norm n1 is not triggered anymore
       .

+oblUnfulfilled(O) <- .print("Unfulfilled ",O).
+sanction(NormId,Event,Sanction) <- .print("Sanction ",Sanction," created for norm ", NormId, " that is ",Event).


{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moise/asl/org-obedient.asl") }
