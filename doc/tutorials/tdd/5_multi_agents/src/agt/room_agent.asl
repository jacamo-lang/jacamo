/*
 * This agent aims to turn on a cooler when the 
 * current temperature is warmer than a target
 * temperature and turn on a heater when the 
 * temperature is colder than a target (both
 * considering a tolerance).
 *
 * It acts upon a HVAC artifact.
 */

tolerance(0.4).

temperature_in_range(T)
	:- not now_is_colder_than(T) & not now_is_warmer_than(T).

now_is_colder_than(T)
	:- temperature(C) & tolerance(DT) & (T - C) > DT.

now_is_warmer_than(T)
	:- temperature(C) & tolerance(DT) & (C - T) > DT.

//!temperature(10). // specified on the jcm file

+!temperature(T): 
	now_is_warmer_than(T) &
	temperature(C)
	<-  
	if (not status(cooling)) {
	    startCooling;
        -+status(cooling);
		.log(warning,C," is too hot -> cooling until ",T);
    }
	!temperature(T);
.

+!temperature(T): 
	now_is_colder_than(T) &
	temperature(C)
	<-  
	if (not status(heating)) {
	    startHeating;
        -+status(heating);
		.log(warning,C," is too hot -> cooling until ",T);
    }
	!temperature(T);
.

+!temperature(T):
	temperature_in_range(T)
	<-  
	if (not status(idle)) {
    	stopAirConditioner;
    	-+status(idle);
		.log(warning,"Temperature achieved: ",T);
	}
    !temperature(T);
.

+!temperature(T)
    <-
    !temperature(T);
.

+!add_preference(T)[source(S)]
	<-
	.abolish(preference(S,_));
	+preference(S,T);
	.findall(X,preference(_,X),L);
	.drop_desire(temperature(_));
	!temperature(math.average(L));
.


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
