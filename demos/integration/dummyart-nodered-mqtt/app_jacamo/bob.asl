// JaCaMo-REST: Integration Demo (Node-RED + MQTT)

!start.
+!start <- .print("Bob is running").

+ns1::message(M)[artifact_name(S)] // perceives msgs from node-red by dummy artifact
  <- .print("New message: ", M);
     .print("Source: ", S);
     if (M \== "start") {
        ns1::act(ack(message(M)),Ret); // and send a msg back
     }
  .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
// { include("$jacamoJar/templates/org-obedient.asl") }
