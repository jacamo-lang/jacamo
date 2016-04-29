package easss.step;

import cartago.*;
import java.math.BigDecimal;

public class GUIConsole extends Artifact {

	private Display display;

	void init() {
    	this.defineObsProperty("numMsg",0);
    	display = new Display();
    	display.setVisible(true);
    }
    
    @OPERATION void printMsg(String msg){
    	String agentName = this.getOpUserName();
    	ObsProperty prop = this.getObsProperty("numMsg");
    	prop.updateValue(prop.intValue()+1);
    	display.addText("Message from "+agentName+": "+msg);
    	display.updateNumMsgField(prop.intValue());
    }    
}



