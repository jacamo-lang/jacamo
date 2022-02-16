package net;

import cartago.*;
import jason.asSyntax.*;

public class MQTTArt extends jacamo.rest.util.DummyArt {

    public void init(String endpoint) {
        defineObsProperty("message", "start");
        super.register(endpoint);
    }

    @OPERATION public void update(String m) {
        getObsProperty("message").updateValues(m);
    }

}
