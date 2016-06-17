
/* Initial beliefs and rules */

ngreetings(0).

// Agent's skills

+!easss
	<-	.println("Achieving easss").
	
+!greet_for_ever
	<-	.my_name(Me);
	   	!greet_task("Do you want a gin tonic?").

-!greet_for_ever
	<- 	.print(err_greet);
	    .wait(1000);
   		!greet_for_ever.

+!greet_task(Message) : ngreetings(N) & N<10
	<- 	.wait(math.random(500)+10);
		-+ngreetings(N+1);
		?jcm__art("msg_console",AId);
	   	printMsg(Message)[artifact_id(AId)];
	   	!greet_task(Message).	
+!greet_task(_)
    <-  -+ngreetings(0);
        .print("I will stop greeting now!").

+!compute
	<-  !join_workspace("server",_);
	
 	    /* discovering and using the calcultor */
	    lookupArtifact("calculator",CalcId);
		computePi(100,Digits) [artifact_id(CalcId)];
		
		/* discovering and using the message console */
		lookupArtifact("msg_console",ConsoleId);
		printMsg(Digits)[artifact_id(ConsoleId)].
		
-!compute
	<- 	.print(err_compute);
	    .wait(1000);
		!compute.
				
+!observe
 	<- !join_workspace("server",_);
 	   lookupArtifact("msg_console",Id);
	   focus(Id);
	   +{ +numMsg(N) <- .println("new message: total number is ",N) }. // add plan
	   