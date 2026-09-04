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

@[test]
+!test_now_is_warmer_than
    <-
    /**
     * Testing the rule now_is_warmer_than in three
     * situations: currentTemp > givenTemp, 
     * currentTemp < givenTemp and currentTemp = givenTemp
     *
     * The default current temperature is 15
     */
    -+temperature(15);
    !assert_false(now_is_warmer_than(20));
    !assert_true(now_is_warmer_than(10));
    !assert_false(now_is_warmer_than(15));
.

/**
 * Test cooler when the temperature is dropping from a warm condition to the target
 */
@[test]
+!test_cool_until_temperature_dropping
    <-
    .add_plan({ 
    +!temperature(T):
        state("cooling")
        <-  
        .log(warning,"Temperature achieved: ",T);
        -state("cooling");
        !temperature(T);
    }, self, begin);
    /*The next plan must be put on the very top of plans*/
    .add_plan({ 
    +!temperature(T): 
        now_is_warmer_than(T) &
        temperature(C)
        <-  
        if (not state("cooling")) {
            +state("cooling");
            .log(warning,C," is too hot -> cooling until ",T);
        }
        !temperature(T);
    }, self, begin);

    -+temperature(15); // The default current temperature is 15 degrees
    !!temperature(10); // We want to reach 10 degrees (this is running in parallel)
    .wait(50); // Give some time to the agent to react
    for ( .range(I,1,10) ) { // Let us check 10x if it is cooling correctly
        ?temperature(C);
        if (C > 10) { // Greater than 10, cooler MUST be on
            !assert_true(state("cooling"));
            -+temperature(C-1); // emulate that the temperature has dropped
        } else { // Not greater than 10, cooler MUST be off
            !assert_false(state("cooling"));
        }
    }
    .drop_desire(temperature(10)); // dropping the desire that is running in parallel
.
