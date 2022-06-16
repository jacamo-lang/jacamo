{ include("tester_agent.asl") }
{ include("air_conditioner.asl") }

/**
 * Test air_conditioner agent in the default max temperature which is 24 degrees
 * At the very beginning the air_conditioner agent has no information about the environment
 * temperature given by the belief "temperature(_)".
 */
@[test]
+!test_ac_cooling_default_setup
    <-
    -+temperature(28); //** INITIAL CONDITION - Let us say the temperature is high **//
    .wait(50); //Give some time to the agent to react
    for ( .range(I,1,10) ) { //** REPEAT THE TEST FOR 10 ITERATIONS **//

        ?temperature(T);
        if (T > 24) { //** TEST CONDITION 1 - cooler is on? **//
            !assert_true(status(cooling));
            -+temperature(T-1); //** EMULATE AGENT IS ACTING UPON THE ENVIRONMENT - Let us say the temperatura dropped 1 degree **//
        } else { //** TEST CONDITION 2 - cooler is off? **//
            !assert_false(status(cooling));
            !assert_true(status(standby));
        }

    }
.

/**
 * Test air_conditioner agent in the default max temperature which is 20 degrees
 * At the very beginning the air_conditioner agent has no information about the environment
 * temperature given by the belief "temperature(_)".
 */
@[test]
+!test_ac_heating_default_setup
    <-
    -+temperature(15); //** INITIAL CONDITION - Let us say the temperature is low **//
    .wait(50); //Give some time to the agent to react
    for ( .range(I,1,10) ) { //** REPEAT THE TEST FOR 10 ITERATIONS **//

        ?temperature(T);
        if (T < 20) { //** TEST CONDITION 1 - cooler is on? **//
            !assert_true(status(heating));
            -+temperature(T+1); //** EMULATE AGENT IS ACTING UPON THE ENVIRONMENT - Let us say the temperatura dropped 1 degree **//
        } else { //** TEST CONDITION 2 - cooler is off? **//
            !assert_false(status(heating));
            !assert_true(status(standby));
        }

    }
.