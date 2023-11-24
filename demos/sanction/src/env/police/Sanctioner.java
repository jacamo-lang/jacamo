// CArtAgO artifact code for project sanction

package police;

import cartago.*;

/**
 * artifact that implements sanctions
 */
public class Sanctioner extends Artifact {
	void init() {
		defineObsProperty("count", 0);
	}

	@OPERATION
	void inc() {
		ObsProperty prop = getObsProperty("count");
		prop.updateValue(prop.intValue()+1);
		signal("tick");
	}

	@OPERATION
	void inc_get(int inc, OpFeedbackParam<Integer> newValueArg) {
		ObsProperty prop = getObsProperty("count");
		int newValue = prop.intValue()+inc;
		prop.updateValue(newValue);
		newValueArg.set(newValue);
	}

}

