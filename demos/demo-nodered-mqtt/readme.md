# Demo JaCaMo + NodeRed + MQTT

## Steps to execute

Crete a docker image for the JaCaMo application:

```
cd jacamo
./gradlew clean copyToLib
docker build -t jacamo-rest/demo/nodered1:0.1 .
```
Run the JaCaMo application and NodeRed usuing docker compose:

```
cd ..
docker-compose up
```

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
