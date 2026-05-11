/**
 * This agent includes the code of the agent under tests
 */
{ include("room_agent.asl") }

!add_mock_plans.

+!add_mock_plans
    <-
    .add_plan({
    +!temperature(T): state("cooling")
    <-
    -state("cooling");
    !temperature(T);
    }, self, begin);

    .add_plan({
    +!temperature(T): now_is_warmer_than(T) & temperature(C)
    <-
    if (not state("cooling")) {
        +state("cooling");
    }
    !temperature(T);
    }, self, begin);

    .add_plan({ 
    +!add_preference(T)[source(S)]
    <-
    .abolish(preference(S,_));
    +preference(S,T);
    .findall(X,preference(_,X),L);
    /*Mock temperature with the average*/
    +temperature(math.mean(L));
    }, self, begin);
.
