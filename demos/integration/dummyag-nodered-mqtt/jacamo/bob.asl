// JaCaMo-REST: Integration Demo (Node-RED + MQTT)

!start.
+!start <- .print("Bob is running").


+message(M)[source(S)] // receive msgs from node-red
  <- .print("New message: ", M);
     .print("Source: ", S);
     .send(S, tell, ack(message(M))); // and send a msg back
  .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
// { include("$jacamoJar/templates/org-obedient.asl") }
