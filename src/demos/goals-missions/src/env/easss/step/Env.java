package easss.step;

import cartago.*;
import java.math.BigDecimal;

public class Env extends Artifact {

	private Display display;
	private boolean locked;
	private int nBarrierParticipants;
	
    void init(int nParticipants) {
    	this.defineObsProperty("numMsg",0);
    	display = new Display();
    	display.setVisible(true);
    	locked = false;
    	nBarrierParticipants = nParticipants;
    	System.out.println("Env ready.");
	}
    
    @OPERATION void printMsg(String msg){
    	String agentName = this.getOpUserName();
    	ObsProperty prop = this.getObsProperty("numMsg");
    	prop.updateValue(prop.intValue()+1);
    	display.addText("Message from "+agentName+": "+msg);
    	display.updateNumMsgField(prop.intValue());
    }
    
    @OPERATION void computePi(int numDigits, OpFeedbackParam<String> res){
    	BigDecimal digits = CalcLib.computePiDigits(numDigits);
    	res.set(digits.toPlainString());
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
    
    @OPERATION void setupBarrier(int nParticipants){
    	nBarrierParticipants = nParticipants;
    }
    
    @OPERATION void synch(){
    	nBarrierParticipants--;
    	await("allArrived");
    }
    
    @GUARD boolean allArrived(){
    	return nBarrierParticipants == 0;
    }
}



