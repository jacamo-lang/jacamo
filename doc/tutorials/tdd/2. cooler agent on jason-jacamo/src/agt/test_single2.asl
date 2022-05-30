{ include("tester_agent.asl") }
{ include("single.asl") }

/**
 * Test thermostat
 */
@[test]
+!test_thermostat
    <-

    //** INITIAL CONDITION - Let us say the temperature starts at 15 degrees **//
    -+temperature(15);
    -maxTemp(_);
    -minTemp(_);
    +maxTemp(10);
    +minTemp(8);

    //** REPEAT THE TEST FOR AN ARBITRARY NUMBER OF ITERATIONS **//
    for ( .range(I,1,20) ) {
        !temperature_control;
        ?temperature(T);

        //** TEST CONDITION 1 - cooler is on? **//
        if (T > 10) {
            !assert_true(status(cooling));

            //** EMULATE AGENT IS ACTING UPON THE ENVIRONMENT - Let us say the temperatura dropped 1 degree **//
            -+temperature(T-1);

        //** TEST CONDITION 2 - cooler is off? **//
        } else {
            !assert_false(status(cooling));

            //** EMULATE ENVIRONMENT HAS ARBITRARILY CHANGED - Let us say the temperatura raised 1 or 2 degrees **//
            .random(X);
            C = math.ceil(X*2);
            -+temperature( T + C );
        }
    }
.
