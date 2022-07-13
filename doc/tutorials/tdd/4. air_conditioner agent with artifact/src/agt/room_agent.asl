/*
 * This agent controls the environment temperature through the 
 * artifact Air Conditioner.
 *
 */

tolerance(0.4).

temperature_in_range(T)
	:- not now_is_colder_than(T) & not now_is_warmer_than(T).

now_is_colder_than(T)
	:- temperature(C) & tolerance(DT) & (T - C) > DT.

now_is_warmer_than(T)
	:- temperature(C) & tolerance(DT) & (C - T) > DT.

+temperature(T) <- .log(warning,"Temperature perceived: ",T).

+!temperature(T): temperature_in_range(T)
	<- 	.log(warning,"Temperature achieved: ",T);
		stopAirConditioner;
.
		
+!temperature(T): now_is_colder_than(T)
	<-  .log(warning,"It is too cold -> heating...");
	    startHeating;
		!temperature(T);
.

+!temperature(T): now_is_warmer_than(T) 
	<-  .log(warning,"It is too hot -> cooling...");
	    startCooling;
		!temperature(T);
.

+!temperature(T) <- !temperature(T).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
