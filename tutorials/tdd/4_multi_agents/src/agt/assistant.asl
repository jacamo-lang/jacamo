/*
 * This agent aims to assist a user,
 * telling her/his preference for the room
 * temperature be known by the room_agent.
 */

//These initial beliefs and plans are being given by the JCM file to run with ./gradlew run
//preferred_temperature(23).
//recipient_agent(room_agent).
//!send_preference.

+!send_preference(R)
    <-
    -+recipient_agent(R);
    !send_preference;
.

+!send_preference:
    preferred_temperature(T) &
    recipient_agent(R)
    <-
    .log(warning,"Sending preference for ",T);
    .send(R,achieve,add_preference(T));
.

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
