/*
 * This simple agent "simulates" as it is cooling
 * the environment.
 *
 * It is assumed that it is cooling when the belief
 * 'cooling' exists.
*/

maxTemp(10)

+!cooler_control:
    temperature(T) & 
    maxTemp(M)
    T > M
    <-
    +cooling;

    // to control a real cooler it could  
    // activate the cooler here as it will
    // perfom this plan while the temperature
    // is above the max

    .log(warning,T," degrees. Cooling...");
.

+!cooler_control:
    cooling
    <-
    -cooling;

    // to control a real cooler it could 
    // deactivate the cooler here
.

+!cooler_control.
