//Agent created automatically

count(0).
!start.

+!start <- .print("Hi").

+test:
    count(C)
    <-
    -+count(C+1);
.


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }