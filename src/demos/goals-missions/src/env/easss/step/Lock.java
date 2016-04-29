package easss.step;

import cartago.*;
import java.math.BigDecimal;

public class Lock extends Artifact {

	private boolean locked;
	
    void init() {
    	locked = false;
	}
    
    @OPERATION void lock(){
    	await("notLocked");
    	locked = true;
    }

    @GUARD boolean notLocked(){
    	return !locked;
    }
    
    @OPERATION void unlock(){
    	locked = false;
    }

}



