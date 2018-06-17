/* auxiliary plans for Moise agents */

// keep focused on schemes that my groups are responsible for
@l_focus_on_my_scheme[atomic]
+schemes(L)[artifact_name(_,GroupName), workspace(_,_,W)]
   <- //cartago.set_current_wsp(W);
      for ( .member(S,L) ) {
         lookupArtifact(S,ArtId)[wid(W)];
         focus(ArtId)[wid(W)];
         .concat(GroupName,".",S,NBName);
         lookupArtifact(NBName,NBId)[wid(W)];
         focus(NBId)[wid(W)];
      }.

// the goal !focus_org_art is produced by the JaCaMo launcher for the agent to focus on initial artifacts
+!jcm::initial_roles([],_).
+!jcm::initial_roles(L,0) <- .print("Error with initial role ",L).

@l_focus_org_art[atomic]
+!jcm::initial_roles([H|T],Try)
   <- !jcm::initial_roles(H,Try);
      !jcm::initial_roles(T,Try).
+!jcm::initial_roles(role(O,H,G,R),Try)
   <- !join_workspace(O,H,WId);
      !jcm::focus_orgBoard(O,WId); // ensures the org board is also focused
      lookupArtifact(G,GId)[wid(WId)];
      focus(GId)[wid(WId)];
      adoptRole(R)[artifact_id(GId)];
      //.print("playing ",R," in ",O,".",G);
   .
-!jcm::initial_roles(L,Try)
   <- //.print("wait a bit to focus on ",L);
      .wait(100);
      !jcm::initial_roles(L,Try-1).

+!jcm::focus_orgBoard(O,WId) : focused(_,O,_).
+!jcm::focus_orgBoard(O,WId) 
   <- lookupArtifact(O,OId)[wid(WId)];
      focus(OId)[wid(WId)].
