/*
 * This simple agent does the XOR operation using 
 * a rule xor/2.
 *
 * A testing agent should check whether this agent
 * is able to produce coherente results from a simple
 * evalution performed by the rule "xor/2".
 */

xor(A, B) :- (A & not B) | (B & not A).

!auto_test.

+!auto_test
    <- 
    .eval(X1, xor(false,false));
    .print(xor(false,false)," = ",X1);

    .eval(X2, xor(false,true));
    .print(xor(false,true)," = ",X2);

    .eval(X3, xor(true,false));
    .print(xor(true,false)," = ",X3);

    .eval(X4, xor(true,true));
    .print(xor(true,true)," = ",X4);
.
