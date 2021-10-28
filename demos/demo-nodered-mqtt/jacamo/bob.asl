// JaCaMo-REST: Integration Demo (Node-RED + MQTT)

// node-red endpoint
uri("http://nodered:1880/mqtt").

!start.
+!start <- .print("Bob is running").

/* Plans */
+!send_msg : uri(U) // send msg to node-red
  <- .print("Sending a greeting message to the MQTT broker");
     .send(U, tell, publish_mqtt("Hi from bob"));
  .

+message(M)[source(S)] // receive msgs from node-red
  <- .print("New message: ", M);
     .print("Source: ", S);
  .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
// { include("$jacamoJar/templates/org-obedient.asl") }
