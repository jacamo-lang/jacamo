!createNew(bob,"sn.ser").
!inc.

+!createNew(A,F)
   <- .wait(3000);
      .print("resuming ",A, " from ",F);
      ia.create_ag_sst(newbob,"sn.ser");
      .wait(2000);
      .kill_agent(newbob);
      .print("new bob killed");
      .wait(2000);
      .print("new bob revival");
      ia.create_ag_sst(newbob,"sn.ser");      
.

+!inc
   <- inc;
      .wait(500);
      !inc.

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
