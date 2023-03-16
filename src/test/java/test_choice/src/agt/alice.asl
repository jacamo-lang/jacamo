
// Agent alice in project test_choice

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("hello world.").

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }

// uncomment the include below to have a agent that always complies with its organization
{ include("$moise/asl/org-obedient.asl") }

+!g5 <- .print("g5 OK").
