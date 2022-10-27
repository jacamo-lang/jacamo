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
        /**
         * To control the room temperature it could  
         * activate a physical cooler here
         */
        +state("cooling");
        .log(warning,C," is too hot -> cooling until ",T);
    }
    !temperature(T);
.

+!temperature(T):
    state("cooling")
    <-  
    .log(warning,"Temperature achieved: ",T);

    /**
     * Deactivating the cooler
     */
    -state("cooling");

    !temperature(T);
.

+!temperature(T)
    <-
    !temperature(T);
.
