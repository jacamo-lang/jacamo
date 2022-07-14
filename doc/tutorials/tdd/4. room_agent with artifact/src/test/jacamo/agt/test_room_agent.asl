/*
 * Test room_agent.
 *
 * This testing agent is including the library
 * tester_agent.asl which comes with assert plans and
 * executes on './gradlew test' the plans that have 
 * the @[test] label annotation
*/

{ include("tester_agent.asl") }
{ include("room_agent.asl") }

@[test]
+!test_temp_control
    <-

    /**
     * Add mock plan for !heat_until to do not call the 
     * artifact, but to produce a mocked answer.
     * Note that since this plan is going to 
     * the beginning of the agent plan list,
     * it is being specified "not now_is_colder_than(T)"
     * to make the contex of this plan less permissive
     */
    .add_plan({ +!temperature(T): now_is_warmer_than(T) & temperature(C)
	<-  .log(warning,"It is too hot -> cooling...");
        -+temperature(C-0.5);
		!temperature(T);
    }, self, begin);

    .add_plan({ +!temperature(T): now_is_colder_than(T) & temperature(C)
	    <-  .log(warning,"It is too cold -> heating...");
            -+temperature(C+0.5);
		    !temperature(T);
    }, self, begin);

    .add_plan({ +!temperature(T): temperature_in_range(T)
	    <- 	.log(warning,"Temperature achieved: ",T);
    }, self, begin);

    !test_heat_until;
    !test_cool_until;
    !!test_results;
.

@[atomic]
+!test_heat_until
    <-
    -+temperature(15);
    !assert_false(temperature_in_range(21));
    !temperature(21);
.

@[atomic]
^!test_heat_until[state(finished)]
    <-
    ?temperature(C);
    +heating_finished_with(C);
.

+!test_cool_until
    <-
    -+temperature(27);
    !assert_false(temperature_in_range(21));
    !temperature(21);
.

^!test_cool_until[state(finished)]
    <-
    ?temperature(C);
    +cooling_finished_with(C);
.

+!test_results
    <-
    .wait(100);
    !assert_true(heating_finished_with(_));
    !assert_true(cooling_finished_with(_));
    ?heating_finished_with(HT);
    ?cooling_finished_with(CT);
    ?tolerance(DT);
    !assert_equals(21,HT,DT);
    !assert_equals(21,CT,DT);
.

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
