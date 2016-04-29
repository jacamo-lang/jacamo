package easss.step;

import cartago.*;
import cartago.tools.GUIArtifact;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;

public class GroupConsole extends GUIArtifact {

	private GroupDisplay display;
	
	public void init(String title) {
    	display = new GroupDisplay(title);
		linkActionEventToOp(display.adoptRole,"adoptRole");
		linkActionEventToOp(display.leaveRole,"leaveRole");
		linkActionEventToOp(display.addScheme,"addScheme");
		linkActionEventToOp(display.removeScheme,"removeScheme");
		display.setVisible(true);
		this.init();
    }
    
    @INTERNAL_OPERATION void adoptRole(ActionEvent ev){
    	this.signal("cmd", "adoptRole",display.groupName.getText(),display.roleToAdopt.getText());
    }    

    @INTERNAL_OPERATION void leaveRole(ActionEvent ev){
    	this.signal("cmd", "leaveRole",display.groupName.getText(),display.roleToLeave.getText());
    }    

    @INTERNAL_OPERATION void addScheme(ActionEvent ev){
    	this.signal("cmd","addScheme", display.groupName.getText(),display.schemeToAdd.getText());
    }    

    @INTERNAL_OPERATION void removeScheme(ActionEvent ev){
    	this.signal("cmd","removeScheme", display.groupName.getText(),display.schemeToRemove.getText());
    }     
}



