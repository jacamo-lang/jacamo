// The code for all agents in the application

{ include("common-cartago.asl") }
{ include("common-moise.asl") }
{ include("common-appl.asl")}
{ include("gui-group.asl") }

/* Initial beliefs and rules */

my_interest(AId) :- product(P,C,Dbis) & terms(P)[artifact_id(AId)] & deadline(D)[artifact_id(AId)] & D < Dbis.
my_interest(AId) :- product("apple",_,_) & terms("apple")[artifact_id(AId)].

/* Initial goals */

!start.

/* Plans */

+!start : my_rft_wsp(RftWsp) & my_rft_board(RftBoard)
   <- !join_wsp(RftWsp);
      !lookup_focus(RftWsp,RftBoard,_).

+task(Terms,ArtName): my_rft_wsp(RftWsp)
   <- .print("new task ",Terms," at ",ArtName);
      !lookup_focus(RftWsp,ArtName,BidArtId);
      !evaluate_rft(Terms,BidArtId).

+!evaluate_rft(Product,AId)
    : my_interest(AId)
   <- .print("I am interested in ",Product);
      ?my_org_wsp(OrgWsp);
      !join_wsp(OrgWsp);
      ?group(GrName)[artifact_id(AId)];
      !lookup_focus(OrgWsp,GrName,GrId);
      adoptRole(tender)[artifact_id(GrId)].

+!evaluate_rft(_,_).

+!bid_done[scheme(SchName)] : not behaviour(malicious) & scheme(SchName)[artifact_id(RFTBidArtId)]
   <- Value = math.random(400)+100;
      bid(Value,"me,my mam and my dad","I can deliver in 5 days")[artifact_id(RFTBidArtId)];
      .print("I placed a bid of ",Value," for ",SchName).
+!bid_done[scheme(SchName)] : behaviour(malicious)
   <- .print("I refuse to bid!!!!"," for ",SchName);
      .fail.

 +won[artifact_name(_,A)] <- .print("I won the RFT ",A).
 +lost[artifact_name(_,A)] <- .print("I lost the RFT ",A).
