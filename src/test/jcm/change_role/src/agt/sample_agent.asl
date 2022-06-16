{ include("$jasonJar/test/jason/inc/test_assert.asl") }

!start.

+!start
   <- .wait(200);
      !assert_true(play(sample_agent,role1,g));
      leaveRole(role1);
      !assert_false(play(sample_agent,role1,g));

      adoptRole(role2);
      !assert_true(play(sample_agent,role2,g));
   .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
