/*
 * XOR rule
*/
xor(A, B) :- (A & not B) | (B & not A).
