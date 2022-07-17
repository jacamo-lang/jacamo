/**
 * Tester agent for room_agent
 *
 * This file should be placed in the folder ./test/jacamo/agt
 * 
 * To run it: $ ./gradlew test --info
 *
 * This testing agent is including the library
 * tester_agent.asl which comes with assert plans and
 * executes on './gradlew test' the plans that have 
 * the @[test] label annotation
*/
{ include("tester_agent.asl") }

/**
 * This agent includes the code of the agent under tests
 */
{ include("room_agent.asl") }

/**
 * Testing rules: now_is_colder_than, now_is_warmer_than and
 * temperature_in_range
 */
@[test]
+!test_temperature_rules
    <-
    -+tolerance(0.4);
    -+temperature(15);
    !assert_false(now_is_colder_than(-5));
    !assert_false(now_is_colder_than(14));
    !assert_false(now_is_colder_than(14.8)); // in the tolerance range
    !assert_false(now_is_colder_than(15.2)); // in the tolerance range
    !assert_true(now_is_colder_than(15.5));
    !assert_true(now_is_colder_than(16));
    !assert_true(now_is_colder_than(40));

    !assert_true(now_is_warmer_than(-5));
    !assert_true(now_is_warmer_than(14));
    !assert_false(now_is_warmer_than(14.8)); // in the tolerance range
    !assert_false(now_is_warmer_than(15.2)); // in the tolerance range
    !assert_false(now_is_warmer_than(15.5));
    !assert_false(now_is_warmer_than(16));
    !assert_false(now_is_warmer_than(40));

    !assert_false(temperature_in_range(-5));
    !assert_false(temperature_in_range(14));
    !assert_true(temperature_in_range(14.8)); // in the tolerance range
    !assert_true(temperature_in_range(15.2)); // in the tolerance range
    !assert_false(temperature_in_range(15.5));
    !assert_false(temperature_in_range(16));
    !assert_false(temperature_in_range(40));
.

/**
 * Test cooler when the temperature is dropping from a warm condition to the target
 */
@[test]
+!test_cool_until_temperature_dropping
    <-
    -+temperature(15); // The default current temperature is 15 degrees
    !!temperature(10); // We want to reach 10 degrees (this is running in parallel)
    .wait(50); // Give some time to the agent to react
    for ( .range(I,1,10) ) { // Let us check 10x if it is cooling correctly
        ?temperature(C);
        .wait(10);
        if (now_is_warmer_than(10)) { // Greater than 10, cooler MUST be on
            !assert_true(status(cooling));
            -+temperature(C-1); // emulate that the temperature has dropped
        } else { // Not greater than 10, cooler MUST be off
            !assert_false(status(cooling));
        }
    }
    .drop_desire(temperature(10)); // dropping the desire that is running in parallel
.

/**
 * Test cooler when the temperature is rising and dropping randomly (with fixed seed)
 */
@[test]
+!test_cool_until_random_temperature
    <-
    -+temperature(18); // Let us say the temperature is 18 degrees
    !!temperature(20); // We want to reach 20 degrees (this is running in parallel)
    .set_random_seed(1); // Make sure this test will be always the same
    .wait(50); // Give some time to the agent to react
    for ( .range(I,1,20) ) { // Let us check 20x if it is cooling correctly
        ?temperature(C);
        .wait(10);
        if (now_is_warmer_than(20)) { // Greater than 20, cooler MUST be on
            !assert_true(status(cooling));
            .random(X); // Emulate that the temperature has dropped
            -+temperature( C - math.ceil(X*2) );
        } else { // Not greater than 20, cooler MUST be off
            !assert_false(status(cooling));
            .random(X); // Emulate that the temperature has risen
            -+temperature( C + math.ceil(X*2) );
        }
    }
    .drop_desire(temperature(20)); // dropping the desire that is running in parallel
.

/**
 * Test heater when the temperature is rising from a cold condition to the target
 */
@[test]
+!test_heat_until_temperature_rising
    <-
    -+temperature(22); // Let us say the temperature is 22 degrees
    !!temperature(28); // We want to reach 28 degrees (this is running in parallel)
    .wait(50); // Give some time to the agent to react
    for ( .range(I,1,10) ) { // Let us check 10x if it is cooling correctly
        ?temperature(C);
        .wait(10);
        if (now_is_colder_than(28)) {
            !assert_true(status(heating));
            -+temperature(C+1); // Emulate that the temperature has risen
        } else { 
            !assert_false(status(heating));
        }
    }
    .drop_desire(temperature(28)); // dropping the desire that is running in parallel
.

/**
 * Test heater when the temperature is rising and dropping randomly (with fixed seed)
 */
@[test]
+!test_cool_until_random_temperature
    <-
    -+temperature(18); // Let us say the temperature is 18 degrees
    !!temperature(25); // We want to reach 25 degrees (this is running in parallel)
    .set_random_seed(2); // Make sure this test will be always the same
    .wait(50); // Give some time to the agent to react
    for ( .range(I,1,20) ) { // Let us check 20x if it is cooling correctly
        ?temperature(C);
        .wait(10);
        if (now_is_colder_than(28)) {
            !assert_true(status(heating));
            .random(X); // Emulate that the temperature has risen
            -+temperature( C + math.ceil(X*2) );
        } else {
            !assert_false(status(heating));
            .random(X); // Emulate that the temperature has dropped
            -+temperature( C - math.ceil(X*2) );
        }
    }
    .drop_desire(temperature(25)); // dropping the desire that is running in parallel
.