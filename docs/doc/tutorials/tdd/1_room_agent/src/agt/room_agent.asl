/*
 * This simple agent tells if now_is_warmer_than a
 * given temperature. The check is performed by the rule
 * `now_is_warmer_than`.
 */

now_is_warmer_than(T) :- temperature(C) & C > T.

/*
 * The default temperature is set to 15
 */
temperature(15).

/*
 * Below is a naive approach to testing, in which the developer
 * creates some tests directly in the source code in order to
 * check if the agent behavior is correct.
 * However, the developer needs to check the print output on 
 * after every code change.
 * Then, the developer needs to remember whether the output
 * corresponds to desired behavior (test pass) or not (test
 * failure). Finally, the tests do not allow for proper
 * continuous integration, e.g., if the code can be affected
 * by changes promoted by another developer, neither developer
 * may be aware of undesired changes to program code behavior.
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
