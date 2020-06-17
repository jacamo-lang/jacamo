{ include("common-cartago.asl") }
{ include("common-appl.asl") }

!setup_and_monitor.

+!setup_and_monitor
    <-  !create_RFTBoard;
        !create_group.

+!create_RFTBoard : my_rft_wsp(RftWsp) & my_rft_board(RftBoard) & my_rft_bad_list(RftBL)
   <- createWorkspace(RftWsp);
      !join_wsp(RftWsp);
      makeArtifact(RftBoard, "rft.RFTBoard",[],_);
      makeArtifact(RftBL, "rft.RFTBlockList",[],_).

+!create_group : my_org_wsp(OrgWsp) & my_org_spec(OS)
   <- .print("Using the organisation ",OS);
        createWorkspace(OrgWsp);
        !join_wsp(OrgWsp);
        ?my_org_name(OrgName);
        makeArtifact(OrgName,"ora4mas.nopl.GroupBoard",[OS, rftOrgMngt, false, true ],GrArtId);
        .my_name(Me);
        setOwner(Me)[artifact_id(GrArtId)];
        .print("group created").

