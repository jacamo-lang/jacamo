/*
 * This simple agent "simulates" a thermostat that triggers 
 * a cooler.
 *
 * It is assumed that it is cooling when the belief
 * 'cooling' exists.
 *
 * A testing agent has to create some conditions to
 * simulate environment conditions in which this agent
 * must be sensitive and reflexive.
 */

max_temp(10).
!cooler_control.

+!cooler_control:
    temperature(T) & 
    max_temp(M) &
    T > M
    <-
    if (not cooling) {
        .log(warning,T," degrees, max is ",M,". Cooling...");

        // to control a real cooler it could  
        // activate the cooler here as it will
        // perfom this plan while the temperature
        // is above the max
        +cooling;
    }

    !cooler_control;
.

+!cooler_control:
    temperature(T) & 
    max_temp(M) &
    cooling
    <-
    .log(warning,T," degrees, max is ",M,". Stopping cooler...");

    // to control a real cooler it could 
    // deactivate the cooler here
    -cooling;
    
    !cooler_control;
.

+!cooler_control
    <-
    !cooler_control;
.
