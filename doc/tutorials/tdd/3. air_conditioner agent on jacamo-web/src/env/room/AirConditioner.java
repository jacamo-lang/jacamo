package room;

import cartago.Artifact;
import cartago.OPERATION;

/**
 * Simple air conditioner artifact which can heat and cool the environment
 *
 */
public class AirConditioner extends Artifact {
    
    /**
     * Heat is on
     */
    @OPERATION
    void heat() {
        return;
    }
}
