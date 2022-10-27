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
{ include("tester_helpers.asl") }

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

@[test]
+!test_multiple_preferences
    <-
    /* 
     * Create a room_agent and two assistants. The assistants
     * ask for 23 and 25 degrees, so the final temperature should
     * be 24 degrees.
     */
    .create_agent(mock_room_agent, "mock_room_agent.asl");
    .create_agent(tims_assistant, "assistant.asl");
    .create_agent(clebers_assistant, "assistant.asl");

    .send(tims_assistant,tell,preferred_temperature(23));
    .send(tims_assistant,tell,recipient_agent(mock_room_agent));
    .send(tims_assistant,achieve,send_preference);
    .send(clebers_assistant,tell,preferred_temperature(25));
    .send(clebers_assistant,tell,recipient_agent(mock_room_agent));
    .send(clebers_assistant,achieve,send_preference);

    /* 
     * Give some time to the room_agent process the information
     * and mocking a result
     */
    .wait(50);
    //.send(mock_room_agent,askOne,temperature(T),temperature(T));
    //!assert_equals(24,T);

    .kill_agent(mock_room_agent);
    .kill_agent(tims_assistant);
    .kill_agent(clebers_assistant);

.
