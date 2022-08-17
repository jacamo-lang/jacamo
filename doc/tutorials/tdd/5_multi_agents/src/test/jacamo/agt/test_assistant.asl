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



//.create_agent(MockAgName, "mock_agent.asl");
//.relevant_plans({+!_},_,LL)

@[test]
+!test_compatibility
    <-
    .create_agent(room_ag, "room_agent.asl");
    .send(room_ag,tellHow,"+?retrieve_plans_for_tell(L) <- .relevant_plans({+!_},_,L).");
    .wait(50);
    .send(room_ag,askOne,retrieve_plans_for_tell(L),L);
    //.relevant_plans({+!_},_,LL);
    .wait(200);
    //.send(room_ag,askOne,plans_for_tell(L), LL);
    retrieve_plans_for_tell(PP) = L;
    for (.member(M,PP))
    {
        .term2string(M,T);
        if (.prefix("kqmlReceivedAskHow",T)) {
            .print("+++++++ ",T);
        } else {
            .print("------- ",T);
        }
    }
    
.

//.relevant_plans({+!_},_,LL)
//.relevant_plans({+_},_,LL)
//.goal_achieved?


+!test_multiple_preferences
    <-
    .create_agent(Room, "room_agent.asl");
    .create_agent(TimAss, "assistant.asl");
.