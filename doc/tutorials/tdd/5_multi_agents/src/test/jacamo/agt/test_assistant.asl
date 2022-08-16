/**
 * Tester agent for assistant
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
{ include("assistant.asl") }

/**
 * Testing send_preference
 */
@[test]
+!test_send_preference
    <-
    +preferred_temperature(23);
    +recipient_agent(test_assistant);
    !send_preference;
.

+!add_preference(T)[source(S)]:
    preferred_temperature(TT)
    <-
    !assert_equals(TT,T);
    !assert_equals(self,S);
.
