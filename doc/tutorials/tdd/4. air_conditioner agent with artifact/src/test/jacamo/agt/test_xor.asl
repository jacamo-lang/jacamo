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

//@[test]
+!test_xor
    <-
    !assert_true(xor(true, false));
    !assert_false(xor(true, true));
    !assert_true(xor(false, true));
    !assert_false(xor(false, false));

    !!test_invalid_inputs_number;

    !assert_true(xor(a, true)); // Should be true because the agent does not believ on "a", so it is false xor with true gives true

    .wait(100);
    !assert_true(catch_test_invalid_inputs_number);

    !assert_true(xor(a, catch_test_invalid_inputs_number)); // Should be true again
    !assert_false(xor(false, true, false)); // The agent does not believ in any xor/3
.

+!test_invalid_inputs_number
    <-
    .log(warning,".eval(Y,xor(0,true));");
    .eval(Y,xor(0,true));
.

-!test_invalid_inputs_number[error(ErrorId), error_msg(Msg), code(CodeBody), code_src(CodeSrc), code_line(CodeLine)]
    <-
    +catch_test_invalid_inputs_number;
    !assert_equals(unknown,ErrorId);
.
