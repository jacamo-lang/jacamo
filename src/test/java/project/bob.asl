{ include("common-cartago.asl") }
{ include("common-moise.asl") }

bel(a,b,10).

+!start 
   <- .print("starting..."); 
      inc;
      .print("end start!");
      .

+!go(X) <- .print("going to ",X); .wait(2000); .print("at ",X,"!").

+tick <- .print("tick").

+!goal2[scheme(S)] <- .print("doing goal2 for scheme ",S).
+!goal3[scheme(S)] <- .print("doing goal3 for scheme ",S).