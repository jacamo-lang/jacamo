// The code for all agents in the application

{ include("common-cartago.asl") }
{ include("common-moise.asl") }
{ include("common-appl.asl")}
{ include("gui-group.asl") }

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */

+!join_group(OrgWsp,GrName,rft(Product,Conditions,Deadline,GrName))
   <- !join_wsp(OrgWsp);
      !lookup_focus(OrgWsp,GrName,GrArtId);
      adoptRole(evaluator)[artifact_id(GrArtId)].

+!bid_evaluated[scheme(SchName)]
    <- //.wait(1000);
    .print("Starting to evaluate bids.");
    ?bids_to_evaluate(SchName,L,Mode,Language,Min,Max)[source(Sender)];
    .print("Bids to evaluate in ",Mode," Language ",Language," Min ", Min," Max ", Max," are ", L);
    // TODO: select and evaluate bids, answer in preference order
    .sort(L,LS);
    .send(Sender,tell,bids_evaluated(SchName,LS));
    .print("Bids ", L, " have been evaluated").
