/*
 * This simple agent does the XOR operation using 
 * a rule xor/2.
 *
 * A testing agent should check whether this agent
 * is able to produce coherente results for this
 * particular logical operation.
*/

xor(A, B) :- (A & not B) | (B & not A).