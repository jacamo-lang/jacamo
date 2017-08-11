package hmi;

import java.awt.event.ActionEvent;

import cartago.INTERNAL_OPERATION;
import cartago.tools.GUIArtifact;

public class SchemeConsole extends GUIArtifact {

    private SchemeDisplay display;
    private String name;
    private int nb=0;

    public void init(String title, String sc) {
        display = new SchemeDisplay(title,sc);
        linkActionEventToOp(display.createScheme,"createScheme");
        linkActionEventToOp(display.deleteScheme,"deleteScheme");
        linkActionEventToOp(display.focuson,"focuson");
        linkActionEventToOp(display.unfocuson,"unfocuson");
        linkActionEventToOp(display.commitMission,"commitMission");
        linkActionEventToOp(display.leaveMission,"leaveMission");
        linkActionEventToOp(display.goalAchieved,"goalAchieved");
        display.setVisible(true);
        name = title;
        nb=0;
        this.init();
    }

   @INTERNAL_OPERATION void createScheme(ActionEvent ev){
        this.signal("cmd", "createScheme",display.schemeToCreate.getText(),display.schemeToCreate.getText()+name+nb++);
    }

    @INTERNAL_OPERATION void deleteScheme(ActionEvent ev){
        this.signal("cmd", "deleteScheme", display.schemeToDelete.getText());
    }

    @INTERNAL_OPERATION void focuson(ActionEvent ev){
        this.signal("cmd", "focusScheme",display.schemeToFocusOn.getText());
    }

    @INTERNAL_OPERATION void unfocuson(ActionEvent ev){
        this.signal("cmd", "unfocusScheme",display.schemeToUnfocus.getText());
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



