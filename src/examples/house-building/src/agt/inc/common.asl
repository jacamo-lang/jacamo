/* auxiliary rules for agents */

i_am_winning(Art)   // check if I placed the current best bid on auction artifact Art
   :- currentWinner(W)[artifact_id(Art)] &
      .my_name(Me) & .term2string(Me,MeS) & W == MeS.

/* auxiliary plans for Cartago */

// try to find a particular artifact and then focus on it
+!discover_art(ToolName)
   <- lookupArtifact(ToolName,ToolId);
      focus(ToolId).
// keep trying until the artifact is found
-!discover_art(ToolName)
   <- .wait(100);
      !discover_art(ToolName).

/* auxiliary rules and plans for Moise */

// keep focused on schemes that my groups are responsible for
+schemes(L)
   <- for ( .member(S,L) ) {
         lookupArtifact(S,ArtId);
         focus(ArtId)
      }.


/* Organisational Plans Required by all agents */

// plans to handle obligations
// obligation to commit to a mission
+obligation(Ag,Norm,committed(Ag,Mission,Scheme),Deadline)
    : .my_name(Ag)
   <- println("I am obliged to commit to ",Mission," on ",Scheme,"... doing so");
      commitMission(Mission)[artifact_name(Scheme)]. 
// obligation to achieve a goal      
+obligation(Ag,Norm,achieved(Scheme,Goal,Ag),Deadline)
    : .my_name(Ag)
   <- //println("I am obliged to achieve goal ",Goal);
      println(" ---> working to achieve ",Goal);
      !Goal[scheme(Scheme)];
      println(" <--- done");
      goalAchieved(Goal)[artifact_name(Scheme)].
// an unknown type of obligation was received
+obligation(Ag,Norm,What,DeadLine)  
   : .my_name(Ag)
   <- println("I am obliged to ",What,", but I don't know what to do!").
