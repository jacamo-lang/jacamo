{ include("tester_agent.asl") }
{ include("cooler.asl") }

/**
 * Test cooler agent in the default max temperature which is 10 degrees
 * At the very beginning the cooler agent has no information about the environment
 * temperature given by the belief "temperature(_)".
 */
//@[test]
+!test_cooler_default_setup
    <-
    //.wait(50); // Give some time for the agent to wakeup
    -+temperature(15); //** INITIAL CONDITION - The temperature is high **//
    .wait(50); //Give some time to the agent to react
    for ( .range(I,1,10) ) { //** REPEAT THE TEST FOR 10 ITERATIONS **//

        ?temperature(T);
        if (T > 10) { //** TEST CONDITION 1 - cooler is on? **//
            !assert_true(cooling);
            -+temperature(T-1); //** EMULATE AGENT IS ACTING UPON THE ENVIRONMENT - Let us say the temperatura dropped 1 degree **//
        } else { //** TEST CONDITION 2 - cooler is off? **//
            !assert_false(cooling);
        }

    }
.

/**
 * Test cooler when the temperature is rising from cool condition to default maxtemp
 */
//@[test]
+!test_cooler_temperature_rising
    <-
    -+temperature(8); //** INITIAL ENVIRONMENT CONDITION **//
    .wait(50); //Give some time to the agent to react
    for ( .range(I,1,10) ) { //** REPEAT THE TEST FOR 10 ITERATIONS **//

        ?temperature(T);
        if (T > 10) { //** TEST CONDITION 1 - cooler is on? **//
            !assert_true(cooling);
            -+temperature(T-0.5); //** EMULATE AGENT IS ACTING UPON THE ENVIRONMENT - Let us say the temperatura dropped 0.5 degree **//
        } else { //** TEST CONDITION 2 - cooler is off? **//
            !assert_false(cooling);
            -+temperature(T+1); //** EMULATE the temperature is rising for some reason **//
        }
    }
.

/**
 * Test cooler agent in a setup different than default
 */
//@[test]
+!test_cooler_another_maxtemp_setup
    <-
    -+temperature(22); //** INITIAL ENVIRONMENT CONDITION **//
    -+max_temp(20); //** SET ANOTHER MAXTEMP **//
    .wait(50); //Give some time to the agent to react
    for ( .range(I,1,10) ) { //** REPEAT THE TEST FOR 10 ITERATIONS **//

        ?temperature(T);
        if (T > 20) { //** TEST CONDITION 1 - cooler is on? **//
            !assert_true(cooling);
            -+temperature(T-0.3); //** EMULATE AGENT IS ACTING UPON THE ENVIRONMENT - Let us say the temperatura dropped 0.3 degree **//
        } else { //** TEST CONDITION 2 - cooler is off? **//
            !assert_false(cooling);
        }
    }
.

//@[test]
+!test_cooler_random_temp
    <-
    -+temperature(18); //** INITIAL ENVIRONMENT CONDITION **//
    -+max_temp(20); //** SET ANOTHER MAXTEMP **//
    .wait(50); //Give some time to the agent to react
    for ( .range(I,1,20) ) { //** REPEAT THE TEST FOR 20 ITERATIONS **//

        ?temperature(T);
        if (T > 20) { //** TEST CONDITION 1 - cooler is on? **//
            !assert_true(cooling);
            .random(X); //** EMULATE ENVIRONMENT HAS ARBITRARILY CHANGED - Let us say the temperatura fallen 1 or 2 degrees **
            -+temperature( T - math.ceil(X*2) );
        } else { //** TEST CONDITION 2 - cooler is off? **//
            !assert_false(cooling);
            .random(X); //** EMULATE ENVIRONMENT HAS ARBITRARILY CHANGED - Let us say the temperatura raised 1 or 2 degrees **
            -+temperature( T + math.ceil(X*2) );
        }
    }
.

