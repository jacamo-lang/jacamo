package hmi;

import java.awt.event.ActionEvent;

import cartago.INTERNAL_OPERATION;
import cartago.tools.GUIArtifact;

public class GroupConsole extends GUIArtifact {

    private GroupDisplay display;
    private String name;
    private int nb=0;

    public void init(String title, String gr) {
        display = new GroupDisplay(title,gr);
        linkActionEventToOp(display.adoptRole,"adoptRole");
        linkActionEventToOp(display.leaveRole,"leaveRole");
        linkActionEventToOp(display.createSubGroup,"createSubGroup");
        linkActionEventToOp(display.deleteSubGroup,"deleteSubGroup");
        linkActionEventToOp(display.focuson,"focuson");
        linkActionEventToOp(display.unfocuson,"unfocuson");
        linkActionEventToOp(display.addScheme,"addScheme");
        linkActionEventToOp(display.removeScheme,"removeScheme");
        display.setVisible(true);
        name = title;
        this.init();
    }

/*
     @OPERATION void printMsg(String msg){

            display.addText(msg);
        }
*/
    @INTERNAL_OPERATION void adoptRole(ActionEvent ev){
        this.signal("cmd", "adoptRole",display.groupName.getText(),display.roleToAdopt.getText());
    }

    @INTERNAL_OPERATION void leaveRole(ActionEvent ev){
        this.signal("cmd", "leaveRole",display.groupName.getText(),display.roleToLeave.getText());
    }

    @INTERNAL_OPERATION void focuson(ActionEvent ev){
        this.signal("cmd", "focusGroup",display.groupToFocusOn.getText());
    }

    @INTERNAL_OPERATION void unfocuson(ActionEvent ev){
        this.signal("cmd", "unfocusGroup",display.groupToUnfocus.getText());
    }

    @INTERNAL_OPERATION void createSubGroup(ActionEvent ev){
        this.signal("cmd", "createSubGroup",display.groupName.getText(),display.subGroupToCreate.getText(),display.subGroupToCreate.getText()+name+nb++);
    }

    @INTERNAL_OPERATION void deleteSubGroup(ActionEvent ev){
        this.signal("cmd", "deleteSubGroup", display.subGroupToDelete.getText());
    }

    @INTERNAL_OPERATION void addScheme(ActionEvent ev){
        this.signal("cmd","addScheme", display.groupName.getText(),display.schemeToAdd.getText());
    }

    @INTERNAL_OPERATION void removeScheme(ActionEvent ev){
        this.signal("cmd","removeScheme", display.groupName.getText(),display.schemeToRemove.getText());
    }
}



