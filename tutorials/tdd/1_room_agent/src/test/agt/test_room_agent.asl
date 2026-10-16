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
