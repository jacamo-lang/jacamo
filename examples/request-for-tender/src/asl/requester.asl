// The code for all agents in the application

{ include("common-cartago.asl") }
{ include("common-moise.asl") }
{ include("common-appl.asl") }
{ include("gui-group.asl") }


//
// rft_board_art_id(_) id of the rft_board artifact
// rft_console_art_id(_) id of the GUI artifact
// rft(gui) if gui, the agent is in interaction with a human through a GUI Artifact
// product(_,_,_) product is composed of a one word term, string of conditions, deadline integer
// current(rft(Product,Conditions,Deadline,GrName,SchName,ControlGrName,ControlSchName));
// wsp_id(_,_) Workspace Name, Workspace ID, created in common-cartago

/* Initial beliefs and rules */

evaluator_candidate(carol1).
evaluator_candidate(carol2).

/* Initial goals */

!start.

/* Plans */

+!test1 : .my_name(alice) <- .wait(2000); .send(bob,tell,hello).
+!start :  my_rft_wsp(RftWsp) & my_org_wsp(OrgWsp) & my_rft_board(RftBoard) & my_rft_bad_list(RftBL)
   <- !join_wsp(OrgWsp);
      !join_wsp(RftWsp);
      !lookup_focus(RftWsp,RftBoard,RFTBoardArtId);
      ?my_org_name(RftOrg);
      !lookup_focus(OrgWsp,RftOrg,RFTOrgArtId);
      !lookup_focus(RftWsp,RftBL,RFTBLArtId);
      +my_rft_org(RFTOrgArtId);
      +my_rft_bl(RFTBLArtId);
      +rft_board_art_id(RFTBoardArtId);
      if (rft(gui)) {  // Creation of the GUI Artifact
        .my_name(Me);
        .concat(Me,"_RFTConsole",CN);
        makeArtifact(CN,"hmi.RFTConsole",[Me],RFTCId);
        focus(RFTCId);
        +rft_console_art_id(RFTCId);
      }
      else {
         +product("apple","red apples, delivery in 30 days, max price 4 USD / Kg",6000);
         //+product("banana","yellow bananas, delivery in 30 days, max price 4 USD / Kg",4000);
         }.

// Interaction with the GUI Artifact
+cmd("addRFT",Terms,Conditions,Deadline)
<- +product(Terms,Conditions,Deadline).

// Internal reasoning
+product(Product,Conditions,Deadline)
   <- .my_name(Me);
      .concat(Me,Product,"RFTGr",GrName);
      .concat(Me,Product,"RFTSch",SchName);
      .concat(Me,Product,"RFTControlGr",ControlGrName);
      .concat(Me,Product,"RFTControlSch",ControlSchName);
      //makeArtifact(GCN,"hmi.GroupConsole",[Me,"bananasRFTGr"],GCId);
      //focus(GCId);
      !startRFT(Product,Conditions,Deadline,GrName,SchName,ControlGrName,ControlSchName).

+!startRFT(Product,Conditions,Deadline,GrName,SchName,ControlGrName,ControlSchName)
   <-
        +current(rft(Product,Conditions,Deadline,GrName,SchName,ControlGrName,ControlSchName));
          // creates the organisation for the RFT
        ?my_org_wsp(OrgWsp);
        ?wsp_id(OrgWsp,OrgWspId);
        cartago.set_current_wsp(OrgWspId);
        ?my_org_spec(OS);
        makeArtifact(GrName,"ora4mas.nopl.GroupBoard",[OS, rftMngt, false, true ],GrArtId);
        focus(GrArtId);
        .my_name(Me);
        setOwner(Me)[artifact_id(GrArtId)];
        ?my_org_name(OrgGrName);
        setParentGroup(OrgGrName)[artifact_id(GrArtId)];
        makeArtifact(ControlGrName,"ora4mas.nopl.GroupBoard",[OS, rftControl, false, true ],ControlGrArtId);
        focus(ControlGrArtId);
        setOwner(Me)[artifact_id(ControlGrArtId)];
        setParentGroup(GrName)[artifact_id(ControlGrArtId)];
        adoptRole(contractingAuthority)[artifact_id(ControlGrArtId)].
  //        adoptRole(contractingAuthority)[artifact_id(GrArtId)].
//      !recruitement(OrgWsp,GrName);
//      .my_name(Me);
//        .concat(Me,Product,"RFTSch",SchName);
//      !rft_published(SchName).


// Organizational Reasoning
//+formationStatus(ok)[artifact_name(_,ControlGrName),artifact_id(GrArtId)]
+formationStatus(ok)[artifact_name(_,ControlGrName),artifact_id(GrArtId)]
    :  current(rft(Product,_,_,_,_,ControlGrName,ControlSchName))
   <-   .print("Group ",ControlGrName," is ok");
        ?my_org_wsp(OrgWsp);
        ?wsp_id(OrgWsp,OrgWspId);
        cartago.set_current_wsp(OrgWspId);
        ?my_org_spec(OS);
        makeArtifact(ControlSchName,"ora4mas.nopl.SchemeBoard",[OS, rftControl, false, true ],ControlSchArtId);
        setArgumentValue(tenders_verified,"Deadline",3000)[artifact_id(ControlSchArtId)];
        addScheme(ControlSchName)[artifact_id(GrArtId)];
        .println("Group ",ControlGrName," is well formed and linked to the scheme ",ControlSchName).

+formationStatus(ok)[artifact_name(_,GrName),artifact_id(GrArtId)]
    : current(rft(Product,_,_,GrName,SchName,_,_))
   <-   .print("Group ",GrName," is ok");
        ?my_org_wsp(OrgWsp);
        ?wsp_id(OrgWsp,OrgWspId);
        cartago.set_current_wsp(OrgWspId);
        ?my_org_spec(OS);
        makeArtifact(SchName,"ora4mas.nopl.SchemeBoard",[OS, rftMngt, false, true ],SchArtId);
        addScheme(SchName)[artifact_id(GrArtId)];
        .println("Group ",GrName," is well formed and linked to the scheme ",SchName).

// organisational goals

+!recruitement[scheme(ControlSchName)] : current(rft(Product,Conditions,Deadline,GrName,_,_,ControlSchName))
   <-   .findall(Ag,evaluator_candidate(Ag),LEval);
        ?my_org_wsp(OrgWsp);
        ?wsp_id(OrgWsp,OrgWspId);
        .send(LEval, achieve, join_group(OrgWsp,GrName,rft(Product,Conditions,Deadline,GrName))).

+!rft_published[scheme(ControlSchName)] : current(rft(Product,Conditions,Deadline,GrName,SchName,ControlGrName,ControlSchName))
   <-   ?rft_board_art_id(RFTBoardArtId);
        announce(Product, Conditions, Deadline, GrName, SchName, AName)[artifact_id(RFTBoardArtId)];
        .print(Product, " is announced! The artifact for bids is ",AName);
        ?my_rft_wsp(RftWsp);
        ?wsp_id(RftWsp,RftWspId);
        !lookup_focus(RftWsp,AName,_);
        .print("RFT for ",Product," started!").

+!rft_published[scheme(SchName)]
   <- .print("RFT Board not created yet, waiting....");
      .wait(100);
      !rft_published.

+!bidding_closed[scheme(SchName)] : scheme(SchName)[artifact_id(RFTBidArtId)]
   <- .wait(state("closed")[artifact_id(RFTBidArtId)]);
      getBids(SBids)[artifact_id(RFTBidArtId)];
      .term2string(List,SBids);
      .print("I have ",.length(List)," bids for ",SchName,": ",List);
      +bids(SchName,List).

//+state(S) : .string(S) <- .print("state is ",S).

+!bid_allocated[scheme(SchName)] : scheme(SchName)[artifact_id(RFTBidArtId)]
    <- ?terms(Term)[artifact_id(RFTBidArtId)];
       ?bids(SchName,List);
       ?group(GrName)[artifact_id(RFTBidArtId)];
      .findall(Ag,play(Ag,evaluator,GrName),LEval);
      .send(LEval,tell,bids_to_evaluate(SchName,List, blind, english, 1, 6));
      .print("Bid allocated for ", Term).

+!decision_taken[scheme(SchName)] : scheme(SchName)[artifact_id(RFTBidArtId)]
   <- ?bids_evaluated(SchName,[bid(Vl,Tender,_,_)|_]);
      notify(Tender)[artifact_id(RFTBidArtId)];
      // TODO: merge lists received by all evaluator, here use just one evaluation
      .print("Selected bid from ",Tender,", value=",Vl).

+!winner_notified.
+!loser_notified.

+!tenders_verified[scheme(ControlSchName)]
     : goalState(_,tenders_verified(Deadline),_,_,_)[artifact_name(_,ControlSchName)]
    <- //.print("Waiting ",Deadline," to verify the tenders....");
       .wait(Deadline);
       ?current(rft(_,_,_,GrName,_,_,ControlSchName));
       .count(play(_,tender,GrName),N);
       .print("I have ",N," tenders in ",GrName);
       // TODO search cardinality in spec of group and use in the place of 5 below
       if (N < 5) {
           .print("Kill it all!!!!!!!!");
       }.

+oblUnfulfilled(obligation(Ag,ngoal(SchName,Mission,Goal),_,_)) : my_rft_bl(RftBL)
   <- .print("Violation on ",Goal," of mission ",Mission," by ",Ag," in scheme ",SchName);
       addBadTender(Ag)[artifact_id(RftBL)].
       // link to goal sanction_applied of CtrlSch


+play(Ag,tender,G)[artifact_id(AId)] : bad_tender(Ag)
   <- .print("Agent ",Ag," will not be allowed to play the role tender since it is in the back list!");
      admCommand(leaveRole(Ag,tender))[artifact_id(AId)].

/*
+list_of_bids(L)[artifact_name(_,ArtName)]
   <- .term2string(List,L);
      .print("I have now ",.length(List)," bids for ",ArtName,": ",List).
*/
