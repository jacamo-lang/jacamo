/*
 * Test XOR rule
*/

{ include("tester_agent.asl") }
{ include("src/main/jason/asl/unit.asl") }

@[test]
+!test_xor
    <-
    !assert_true(xor(true, false));
    !assert_false(xor(true, true));
    !assert_true(xor(false, true));
    !assert_false(xor(false, false));
.
