+!heat_control:
    temperature(T) & T > 10
    <-
    //fan on
    +cooling;
    .log(warning,T," degrees. Cooling...");
.

+!heat_control:
    cooling
    <-
    -cooling;
.

+!heat_control.
