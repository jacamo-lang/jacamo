/*
 * Returns the list of achievement plan triggers (the ones that starts with +!).
 * Each trigger is the signature of a plan and its arguments and extra data between brackets
 */
+?retrieve_achievement_plans(Plans) : 
    .relevant_plans({+!_},LP,LL) 
    <- 
    .findall(T, .member(P,LP) & P = {@L +!T : C <- B} & Label = L, Plans);
.

/*
 * Returns the list of add belief plan triggers (the ones that starts with +).
 * Each trigger is the signature of a plan and its arguments and extra data between brackets
 */
+?retrieve_add_belief_plans(Plans) : 
    .relevant_plans({+_},LP,LL) 
    <- 
    .findall(T, .member(P,LP) & P = {@L +T : C <- B} & Label = L, Plans);
.

/*
 * Returns on the second argument (X) a boolean that says if a given plan (P)
 * is in the list of relevant plans of the agent or not
 */
+?is_achievement_plan(P,X) 
    <- 
    ?retrieve_achievement_plans(L); 
    .eval(X,.member(P,L));
.
