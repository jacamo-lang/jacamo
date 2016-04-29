package easss.step;

import cartago.*;
import java.math.BigDecimal;

public class Barrier extends Artifact {

	private int nBarrierParticipants;
	
    void init(int nParticipants) {
    	this.nBarrierParticipants = nParticipants;
	}
    
    @OPERATION void synch(){
    	nBarrierParticipants--;
    	await("allArrived");
    }
    
    @GUARD boolean allArrived(){
    	return nBarrierParticipants == 0;
    }
}



