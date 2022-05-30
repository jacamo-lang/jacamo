maxTemp(22).
minTemp(20).
status(standBy).

/*
 * If the current temperature is above the max -> turn the cooler on.
 */
+!temperature_control:
    temperature(T) & 
    maxTemp(M) &
    T > M
    <-
    .abolish(status(_));
    +status(cooling);

    // operate somehow to cool the environment 

    .log(warning,T," degrees. Cooling...");
.

/*
 * If the current temperature is below the min -> turn the heater on.
 */
+!temperature_control:
    temperature(T) & 
    minTemp(M) &
    T < M
    <-
    .abolish(status(_));
    +status(heating);

    // operate somehow to heat the environment 

    .log(warning,T," degrees. Heating...");
.

+!temperature_control:
    not status(standBy)
    <-     
    .abolish(status(_));
    +status(standBy);
.

+!temperature_control.
