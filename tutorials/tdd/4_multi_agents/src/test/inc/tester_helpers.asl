/*
 * A plan that asks to an agent A if a given plan (P) is in the list of relevant 
 * plans of the agent or not a boolen (X) represents the result of this consult.
 */
+!is_achievement_plan(A,P,X)
    <-
    .send(A,askOne,is_achievement_plan(P,X),is_achievement_plan(P,X));
.

/*
 * A mock agent can answer to another agent whether it is already in idle
 * mode or not, which is relevant to know if the agent has already started
 * completely.
 */
+!start_mock_agent(MockAgName, File) :
    .my_name(ME)
    <-
    .create_agent(MockAgName, File);
    .send(MockAgName, tell, mock_owner(ME));
    // if the mock was already sleeping, wait it wake up first
    .wait( not sleeping(MockAgName), 300, _ );
    // wait the mock finish some task and tell that it is now sleeping
    .wait( sleeping(MockAgName) );
.    
