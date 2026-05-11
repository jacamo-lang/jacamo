/* Artifact created automatically by jacamo-web */

package devices;

import cartago.*;

@ARTIFACT_INFO(outports = { @OUTPORT(name = "out-1") })

public class HVAC extends Artifact {

    void init(double initialTemperature){
        defineObsProperty("state","idle");
        defineObsProperty("temperature",initialTemperature);
        log("Temperature: "+getObsProperty("temperature").doubleValue());
    }

    @OPERATION void startCooling(){
        log("startCooling");
        getObsProperty("state").updateValue("cooling");
        this.execInternalOp("updateTemperatureProc",-0.5);
    }

    @OPERATION void stopAirConditioner(){
        log("stopAirCond");
        getObsProperty("state").updateValue("idle");
    }

    @INTERNAL_OPERATION void updateTemperatureProc(double step){
        ObsProperty temp = getObsProperty("temperature");
        ObsProperty state = getObsProperty("state");
        while (!state.stringValue().equals("idle")){
            temp.updateValue(temp.doubleValue() + step);
            log("Temperature: "+temp.doubleValue());
            this.await_time(100);
        }
    }
}
