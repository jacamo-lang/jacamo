package hmi;

import java.awt.event.ActionEvent;

import cartago.INTERNAL_OPERATION;
import cartago.tools.GUIArtifact;

public class RFTConsole extends GUIArtifact {

    private RFTDisplay display;
    private String name;

    public void init(String title) {
        display = new RFTDisplay(title);
        linkActionEventToOp(display.addRFT,"addRFT");
        display.setVisible(true);
        name = title;
        this.init();
    }

/*
     @OPERATION void printMsg(String msg){

            display.addText(msg);
        }
*/

    @INTERNAL_OPERATION void addRFT(ActionEvent ev){
        this.signal("cmd","addRFT", display.rftToAdd.getText(), display.conditionToAdd.getText(),new Integer(display.deadlineToAdd.getText()));
    }
}



