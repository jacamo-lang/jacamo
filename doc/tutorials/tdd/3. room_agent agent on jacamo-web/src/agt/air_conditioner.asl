/*
 * This simple agent "simulates" an Air Conditioner that cool or heat
 * the environment according to the max and min temperatures.
 *
 * It is assumed that it is cooling when the belief
 * 'status(cooling)' exists, or it may be 'status(heating)', or 
 * 'status(standby)'.
 *
 * A testing agent has to create some conditions to
 * simulate environment conditions in which this agent
 * must be sensitive and reflexive.
 */

max_temp(24).
min_temp(20).
status(standby).
!temperature_control.

/*
 * If the current temperature is above the max -> turn the cooler on.
 */
+!temperature_control:
    temperature(T) & 
    max_temp(M) &
    T > M
    <-
    if (not status(cooling)) {
        .log(warning,T," degrees, max is ",M,". Cooling...");

        // to control a real cooler it could  
        // activate the cooler here as it will
        // perfom this plan while the temperature
        // is above the max
        .abolish(status(_));
        +status(cooling);
    }

    !temperature_control;
.

/*
 * If the current temperature is below the min -> turn the heater on.
 */
+!temperature_control:
    temperature(T) & 
    min_temp(M) &
    T < M
    <-
    if (not status(heating)) {
        .log(warning,T," degrees, min is ",M,". Heating...");

        // to control a real cooler it could  
        // activate the cooler here as it will
        // perfom this plan while the temperature
        // is above the max
        .abolish(status(_));
        +status(heating);
    }

    !temperature_control;
.

+!temperature_control:
    not status(standby)
    <-     
    .abolish(status(_));
    +status(standby);

    !temperature_control;
.

+!temperature_control
    <-
    !temperature_control;
.
