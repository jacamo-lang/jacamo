// Agent dummy

{ include("$jacamoJar/templates/common-cartago.asl") }

/*
 * By Joao Leite
 * Based on implementation developed by Rafael Bordini, Jomi Hubner and Maicon Zatelli
 */


/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .wait(500); skip; !start.
