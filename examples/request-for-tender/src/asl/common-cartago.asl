// Set of plans for joining workspaces (local or remote ones)
+!join_wsp(W)
   <- joinWorkspace(W,Id);
      +wsp_id(W,Id);
      .print("I am in the workspace ",W).

-!join_wsp(W)
   <- .print("waiting for the workspace...");
      .wait(500);
      !join_wsp(W).

+!join_remote_wsp(W,Host)
   <-   joinRemoteWorkspace(W,Host,Id);
        +wsp_id(W,Id);
        .print("I am in the workspace ",W).

-!join_remote_wsp(W,Host)
   <- .print("waiting for the workspace...");
      .wait(500);
      !join_remote_wsp(W,Host).

// Set of plans for looking for an artifact in a Workspace
@llc[atomic]
+!lookup_focus(WspName,AName,AId) : wsp_id(WspName,WspId)
   <- ?current_wsp(OldWspId,_,_);
      cartago.set_current_wsp(WspId);
      lookupArtifact(AName, AId);
      focus(AId);
      cartago.set_current_wsp(OldWspId);
      .print("I am focusing on ",AName).

-!lookup_focus(WspName,AName,AId)
   <- .print("waiting for the artifact ",AName,"...");
      .wait(500);
      !lookup_focus(WspName,AName,AId).
