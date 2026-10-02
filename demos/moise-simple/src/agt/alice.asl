/*

Agent alice wants to coordinate some tasks with bob and tom.

The goals for the tasks are: a,b,c,d,e,f

With the following dependencies:

                        D 
  A -----> C ---------- ^ ---> E
           ^            |
           |            |
           |            |
  B ------ |            | ---> F

C depends on A and B
D depends on C
E and F depends on D

and the following task allocation:
    A: bob 
    B: alice
    C: tom  
    D: alice
    E: tom
    F: bob

for that, 3 roles are defined:

    A, F -> role r1, for bob
    B, D -> role r2, for alice
    C, E -> role r3, for tom

*/


!start.

+!start
   <- // create an organsation to manage the coordination
      makeArtifact(tml,"ora4mas.simple.SimpleOrgBoard",[],OIa);
      focus(OIa);
      createGroup(grp1,Gid); // create a group for the agents committed to the tasks
      focus(Gid);

      createScheme(s1,Sid); // create a scheme to manage task execution
      focus(Sid);
      addScheme(s1); // assigns the scheme to the group grp1

      // create the graph of dependencies
      addGoal(c,dep(and,[a,b]));
      addGoal(d,dep(and,[c]));
      addGoal(e,dep(and,[d]));
      addGoal(f,dep(and,[d]));

      // add a norm to oblige roles to commit to goals
      addNorm(obligation,r1,a);
      addNorm(obligation,r1,f);
      addNorm(obligation,r2,b);
      addNorm(obligation,r2,d);
      addNorm(obligation,r3,c);
      addNorm(obligation,r3,e);

      adoptRole(r2); // alice adopts role r2, which is obliged to commit to goals b and d

   .

// plans for the tasks alocated to alice (b and d)

+!b <- .wait(1000); .print("doing goal b"); .wait(1000). // triggered by obligation based on the commitment to g1
+!d <- .print("doing goal d");  .wait(1000).

/*+goalState(s1,g2,_,_,satisfied)
   <- .print("Finished!");
       destroyScheme(s1);
       destroyGroup(grp1);
   . */

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
{ include("$moiseJar/asl/org-obedient.asl") }
