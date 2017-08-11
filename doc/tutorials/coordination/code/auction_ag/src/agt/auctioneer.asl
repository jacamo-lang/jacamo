!start. // initial goal

+!start : true
   <- .broadcast(tell, auction(service, flight_ticket(paris,athens,"15/12/2015")));
      .broadcast(tell, auction(service, flight_ticket(athens,paris,"18/12/2015"))).


+bid(Service, _)     // receives bids and checks for new winner
   :  .findall(b(V,A),bid(Service,V)[source(A)],L) & // L is a list of all bids, e.g.: [b(77.7,alice), b(91.7,giacomo), ...]
      .length(L,4)  // all 4 expected bids was received, announce the winner
   <- .min(L,b(V,W));
      .print("Winner for ", Service, " is ",W," with ", V);
      .broadcast(tell, winner(Service,W)).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
