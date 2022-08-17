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

/**
 * Testing compatibility between the assistant and the room_agent
 * It is expected that the room_agent has plans referred on .send
 * command executed by the assistant.
 */
@[test]
+!test_compatibility
    <-
    /* A test_room_agent agent is being created instead of
     * room_agent because the test_room_agent has all plans
     * that room_agent has and also it is adding test_helpers.asl
     */
    .create_agent(mock_room_ag, "mock_room_agent.asl");
    !is_achievement_plan(mock_room_ag,add_preference(_),X);
    !assert_true(X);
    !is_achievement_plan(mock_room_ag,non_existing_plan(_,_),Y);
    !assert_false(Y);
    .kill_agent(mock_room_ag);
.

@[test]
+!test_multiple_preferences
    <-
    .create_agent(mock_room_agent, "mock_room_agent.asl");
    .wait(100);

    .create_agent(tims_assistant, "mock_assistant.asl");
    .send(tims_assistant,tell,preferred_temperature(23));
    .send(tims_assistant,tell,recipient_agent(mock_room_agent));
    .send(tims_assistant,achieve,send_preference);
    .wait(100);

    .create_agent(clebers_assistant, "mock_assistant.asl");
    .send(clebers_assistant,tell,preferred_temperature(25));
    .send(clebers_assistant,tell,recipient_agent(mock_room_agent));
    .send(clebers_assistant,achieve,send_preference);

    .wait(100);
    .send(mock_room_agent,askOne,temperature(T),temperature(T));
    !assert_equals(24,T);
.