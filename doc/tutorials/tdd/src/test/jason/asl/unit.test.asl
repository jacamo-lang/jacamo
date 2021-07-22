/*
 * Test XOR rule
*/

{ include("tester_agent.asl") } 

xor(A, B) :- (A & not B) | (B & not A).

@[test]
+!test_xor
    <-
    !assert_true(xor(true, false));
    !assert_false(xor(true, true));
    !assert_true(xor(false, true));
    !assert_false(xor(false, false));
.
