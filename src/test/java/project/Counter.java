package project;
import cartago.Artifact;
import cartago.OPERATION;
import cartago.ObsProperty;

public class Counter extends Artifact {

    @OPERATION void init(String name, int v){
        defineObsProperty("name",  name);
        defineObsProperty("count", v);
        System.out.println("Created artifact "+name+" with initial value "+v);
    }

    @OPERATION void inc() {
        ObsProperty prop = getObsProperty("count");
        prop.updateValue(prop.intValue()+1);
        signal("tick");
    }
}
