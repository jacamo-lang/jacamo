// CArtAgO artifact code for project distribution1

package myenv;

import cartago.*;

public class WhiteBoard extends Artifact {
	
	void init(String m) {
		System.out.println(m);
		defineObsProperty("count", 0);
	}
	
	@OPERATION
	void message(String m) {
		ObsProperty prop = getObsProperty("count");
		System.out.println( prop.intValue()+"> "+getOpUserName()+": "+m);
		prop.updateValue(prop.intValue()+1);
		signal("tick");
	}
}

