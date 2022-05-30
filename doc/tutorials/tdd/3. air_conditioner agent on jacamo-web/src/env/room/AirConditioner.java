package room;

import cartago.*;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

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
        cfps.clear();
    }
}
