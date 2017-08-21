package example;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;

public class Test extends Artifact {
    @OPERATION
    void getTrueBoolean(OpFeedbackParam<Boolean> b) {
        b.set(true);
    }
}
