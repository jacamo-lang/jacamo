{ include("$jasonJar/test/jason/inc/test_assert.asl") }

!start.

+!start
<-
    makeArtifact("test", "example.Test");
    getTrueBoolean(B);
    !assert_equals(B,true);
    //.list_plans;
    if (B) {
        .print(B);
    } else {
        .print("nope");
    }.
