+bid(S,_) : not .desire( bid(S) ) <- !bid(S).

+!bid(S)
   <- .wait(1000);
      .findall(V,bid(S,V),L);
      .min(L,MV);
      .broadcast(tell,bid(S, MV*0.9));
      .print("Bid ",MV*0.9, " for ",S).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
