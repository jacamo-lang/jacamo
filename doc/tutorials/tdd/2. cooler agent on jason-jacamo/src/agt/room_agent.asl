/*
 * This agent aims to turn on a cooler when the 
 * current temperature is warmer than a target
 * temperature.
 */

now_is_warmer_than(T) :- temperature(C) & C > T.
//!temperature(10). // specified on the mas2j file

+!temperature(T): 
	now_is_warmer_than(T) &
	temperature(C)
	<-  
	.log(warning,C," is too hot -> cooling until ",T);
	if (not cooling) {

	    /**
	 	 * To control the room temperature it could  
     	 * activate a physical cooler here
	 	 */
        +cooling;
    }
    .wait(50);
	!temperature(T);
.

+!temperature(T):
	cooling
	<-  
	.log(warning,"Temperature achieved: ",T);

    /**
	 * Deactivating the cooler
	 */
    -cooling;

    !temperature(T);
.

+!temperature(T)
    <-
    !temperature(T);
.
