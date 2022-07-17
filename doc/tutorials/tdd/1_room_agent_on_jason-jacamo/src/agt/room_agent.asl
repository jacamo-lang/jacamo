/*
 * This simple agent tells if now_is_warmer_than a
 * given temperature, which is performed by a rule
 * now_is_warmer_than/1.
 */

now_is_warmer_than(T) :- temperature(C) & C > T.

/*
 * The default temperature is set to 15
 */
temperature(15).

/*
 * Here a common approach in which the developer creates
 * some tests to check if the agent behaviour is correct.
 *
 * However, it requires the attention of the developer on 
 * the output on every code change.
 * It also requires some reasoning from the developer on
 * reminding which is the correct output according to the 
 * desired logic.
 * Finally, it is not good on code integration, e.g., if this
 * agent can be affected by changes promoted by another 
 * developer, neither the developer of this agent and of the 
 * another agent are aware of such an integration problem.
 */
!auto_test.

+!auto_test:
    temperature(C)
    <- 
    .print("Current temperature: ",temperature(C));

    .eval(X1, now_is_warmer_than(20));
    .print(now_is_warmer_than(20)," = ",X1);

    .eval(X2, now_is_warmer_than(10));
    .print(now_is_warmer_than(10)," = ",X2);

    .eval(X3, now_is_warmer_than(15));
    .print(now_is_warmer_than(15)," = ",X3);
.
