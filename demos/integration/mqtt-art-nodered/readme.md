# Demo JaCaMo + Node-RED + MQTT (by an specific artifact)

This demo presents an integration scenario where one agent leverages the MQTT protocol using perception and action. An artifact was developed for this purpose.


## Understanding the demo

The scenario consists of four entities:

* A JaCaMo application where agent Bob runs and an artifact for MQTT is created
* Node-RED
* MQTT Broker
* You

In this scenario, the agent interacts with an _MQTT broker_. [MQTT](http://mqtt.org/) is a lightweight protocol for messaging transport in a publish/subscribe pattern, and an MQTT broker is a server that receives messages from _publishers_ and routes them to the appropriate _subscribers_. MQTT clients publish and subscribe to so-called _topics_. In this demo we use the topic __mqtt/jacamo/bob__.

A central entity to this scenario is the [Node-RED](https://nodered.org/), an integration tool that provides a browser-based editor to wire together hardware devices, APIs, and online services using a wide range of _nodes_. Here, it is responsible for integrating the JaCaMo app and the MQTT broker. Its job in this demo is:

1. when something is published at _mqtt/jacamo/bob_, NodeRed updates the observable properties of the MQTT artifact (called `mqtt`) so that Bob perceives the publication.

2. when Bob acts on `mqtt` publishing something, NodeRed notices the action forwards the publication to _mqtt/jacamo/me_.

## Steps to execute

```
docker-compose up
```

It starts the NodeRed and the JaCaMo application, both based on available Docker images. The sources of the JaCaMo application are placed in the folder `app_jacamo`.

The output is something like

```
mas_1      | [JCMRest] JaCaMo Rest API is running on http://172.19.0.2:8080/.
mas_1      | Jason Http Server running on http://172.19.0.2:3272
mas_1      | [Cartago] Workspace wmqtt created.
mas_1      | [Cartago] artifact mqtt: net.MQTTArt("http://nodered:1880/act") at wmqtt created.
mas_1      | [bob] joinned workspace /main/wmqtt
mas_1      | [bob] focusing on artifact mqtt (at workspace /main/wmqtt) using namespace ns1
mas_1      | [bob] Bob is running
mas_1      | [bob] New message: start
mas_1      | [bob] Source: mqtt
```

## Interacting with agent Bob

- Open the [HiveMQ client web page](http://www.hivemq.com/demos/websocket-client) and click on *connect*.

- In the HiveMQ client web page, subscribe to notification on the topic _mqtt/jacamo/me_:

![MQTT](figures/mqtt4.png)

- To send a message to Bob, fill the form as follows (note the topic `mqtt/jacamo/bob` that Bob is listening to):

![MQTT](figures/mqtt1.png)

When you click on *Publish*, Bob prints out a message:

```
mas_1      | [bob] New message: test_msg_1
mas_1      | [bob] Source: mqtt
```

and sends back a message to `mqtt` that can be noticed in the topic _mqtt/jacamo/me_:

![MQTT](figures/mqtt5.png)

Note: You can inspect Bob's mind at [http://localhost:3272](http://localhost:3272).

Stop the application:

```
docker-compose down
```

## Implementation

The MAS starts with the agent Bob and an artifact that is coded to represent the MQTT in for the agents (source at `src/env/net/MQTTArt.java`).

The integration is essentially based on NodeRed. It is responsible for:

1. notifying Bob (via artifact) about new publications at _mqtt/jacamo/bob_,

2. publishing at _mqtt/jacamo/me_ all actions performed on the artifact.

The integration code (i.e., the flows in NodeRed) can be seen in the [NodeRed web interface](http://127.0.0.1:1880) and are commented below.

#### MQTT to artifact (and thus to Bob)

NodeRed listens to the MQTT topic _mqtt/jacamo/bob_ and, for each new publication, it updates the observable property `message`. It is performed by a `POST` to `http://mas:8080/workspaces/wmqtt/artifacts/mqtt/operations/update/execute`.

#### Bob to MQTT

When Bob performs `act` on the artifact, the artifact dispatches the arguments to (`http://nodered:1880/act`). NodeRed listens the `act` end-point and publishes at _mqtt/jacamo/me_ the content of the message.
