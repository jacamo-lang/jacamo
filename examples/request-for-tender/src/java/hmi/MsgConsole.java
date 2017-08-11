package hmi;

import cartago.Artifact;
import cartago.OPERATION;

public class MsgConsole extends Artifact {

    private MsgDisplay display;

    void init(String name) {
        display = new MsgDisplay(name);
        display.setVisible(true);
    }

    @OPERATION void printMsg(String msg){
        display.addText(msg);
    }
}



