/*
 * Test XOR agent.
 *
 * This testing agent is including the library
 * tester_agent.asl which comes with assert plans and
 * executes on './gradlew test' the plans that have 
 * the @[test] label annotation
*/

{ include("tester_agent.asl") }
{ include("xor.asl") }

@[test]
+!test_xor
    <-
    !assert_true(xor(true, false));
    !assert_false(xor(true, true));
    !assert_true(xor(false, true));
    !assert_false(xor(false, false));
.
