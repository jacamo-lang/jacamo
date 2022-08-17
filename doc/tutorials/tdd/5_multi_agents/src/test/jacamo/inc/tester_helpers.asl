/*
 * A plan that asks to an agent A if a given plan (P) is in the list of relevant 
 * plans of the agent or not a boolen (X) represents the result of this consult.
 */
+!is_achievement_plan(A,P,X)
    <-
    .send(A,askOne,is_achievement_plan(P,X),is_achievement_plan(P,X));
.
