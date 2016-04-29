// Agent sample_agent in project distribution1

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

/* Initial goals */

!start.

+!start <- .wait(1000); message("hello world."); !start.
