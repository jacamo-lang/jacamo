/*
 * This agent aims to turn on a cooler when the 
 * current temperature is warmer than a target
 * temperature.
 */

now_is_warmer_than(T) :- temperature(C) & C > T.
//!temperature(15). // specified on the jcm file

+!temperature(T): 
    now_is_warmer_than(T) &
    temperature(C)
    <-  
    if (not state("cooling")) {
        startCooling;
        .log(warning,C," is too hot -> cooling until ",T);
    }
    !temperature(T);
.

+!temperature(T):
    state("cooling")
    <-  
    stopAirConditioner;
    .log(warning,"Temperature achieved.");
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
    !temperature(math.mean(L));
.

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
