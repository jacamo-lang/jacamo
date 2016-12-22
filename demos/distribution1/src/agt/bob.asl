{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

/* Initial goals */

!start.
!send(0).

+!start <- .wait(1000); message("hello world."); !start.

+!send(X)  <- .wait(1500); .send(alice, tell, hello(X)); !send(X+1).
