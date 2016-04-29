package easss.step;

import cartago.*;
import cartago.tools.GUIArtifact;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;

public class SchemeConsole extends GUIArtifact {

	private SchemeDisplay display;
	
	public void init(String title) {
    	display = new SchemeDisplay(title);
		linkActionEventToOp(display.commitMission,"commitMission");
		linkActionEventToOp(display.leaveMission,"leaveMission");
		linkActionEventToOp(display.goalAchieved,"goalAchieved");
		display.setVisible(true);
		this.init();
    }
    
    @INTERNAL_OPERATION void commitMission(ActionEvent ev){
    	this.signal("cmd", "commitMission",display.schemeName.getText(),display.missionToCommit.getText());
    }    

    @INTERNAL_OPERATION void leaveMission(ActionEvent ev){
    	this.signal("cmd", "leaveMission", display.schemeName.getText(),display.missionToLeave.getText());
    }    

    @INTERNAL_OPERATION void goalAchieved(ActionEvent ev){
    	this.signal("cmd","goalAchieved", display.schemeName.getText(),display.goalToAchieve.getText());
    }    

}



