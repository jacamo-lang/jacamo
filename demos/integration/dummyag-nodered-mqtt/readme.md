# Demo JaCaMo + Node-RED + MQTT

This demo presents an integration scenario where one agent leverages the MQTT protocol using ACL messages.


## Understanding the demo

The scenario consists of four entities:

* A JaCaMo application where agent Bob runs
* Node-RED
* MQTT Broker
* You

In this scenario, the agent interacts with an _MQTT broker_. [MQTT](http://mqtt.org/) is a lightweight protocol for messaging transport in a publish/subscribe pattern, and an MQTT broker is a server that receives messages from _publishers_ and routes them to the appropriate _subscribers_. MQTT clients publish and subscribe to so-called _topics_. In this demo we use the topic __mqtt/jacamo/bob__.

A central entity to this scenario is the [Node-RED](https://nodered.org/), an integration tool that provides a browser-based editor to wire together hardware devices, APIs, and online services using a wide range of _nodes_. Here, it is responsible for integrating the JaCaMo app and the MQTT broker. Its job in this demo is:

- create a dummy agent representing the MQTT in the MAS, so that agent Bob sees MQTT as an agent (named `dmqtt` in this demo)

- send an ACL message to Bob whenever something is published at _mqtt/jacamo/bob_.

- when bob sends a message to `dmqtt`, NodeRed receives the message and publishes the message's content at _mqtt/jacamo/me_.

## Steps to execute

```
docker-compose up
```

It starts the NodeRed and the JaCaMo application, both based on available Docker images. The sources of the JaCaMo application are in placed in the folder `jacamo`.

The output is something like

```
nodered_1  | 15 Jul 12:29:10 - [info] Starting flows
nodered_1  | 15 Jul 12:29:10 - [info] Started flows
nodered_1  | 15 Jul 12:29:10 - [info] Server now running at http://127.0.0.1:1880/
nodered_1  | 15 Jul 12:29:10 - [info] [mqtt-broker:7d5786c9.7bf498] Connected to broker: mqtt://broker.hivemq.com:1883
bob_1      | [NetworkListener] Started listener bound to [{0}]
bob_1      | [HttpServer] [{0}] Started.
bob_1      | [JCMRest] JaCaMo Rest API is running on http://172.24.0.3:8080/.
bob_1      | CArtAgO Http Server running on http://172.24.0.3:3273
bob_1      | Jason Http Server running on http://172.24.0.3:3272
bob_1      | [bob] Bob is running
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
mas_1      | [bob] Source: dmqtt
```

and sends back a message to `dmqtt` that can be noticed in the topic _mqtt/jacamo/me_:

![MQTT](figures/mqtt5.png)

Note: You can inspect bob's mind at [http://localhost:3272](http://localhost:3272).

Stop the application:

```
docker-compose down
```

## Implementation

__under construction__

For now, see the source code and the NodeRed [flows](http://127.0.0.1:1880).