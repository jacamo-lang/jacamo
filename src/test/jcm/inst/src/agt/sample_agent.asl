// Agent sample_agent in project inst

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start
   <- .print("hello world.");
      inc;
      //.wait(5000);
      //inc;
   .

+obligation(A,R,G,D) : .my_name(A)
   <- .print("I am obliged to ",G," doing inc to fulfill");
      inc;
   .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
