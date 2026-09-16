package display;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.ObsProperty;

public class GridDisplay extends Artifact {

    private Display display;

    void init(String name) {
        // creates an observable property called numMsg
        display = new Display(name);
        display.setVisible(true);
    }

    // implements an operation available to the agents
    @OPERATION void printChar(int line, int col, String c) {
        display.setChar(line,col,c);
    }

    static class Display extends JFrame {

        int lines = 3;
        int columns = 15;
        
        private JTextArea[][] text = new JTextArea[lines][columns];
        private JLabel numMsg;
        private static int n = 0;

        public Display(String name) {
            setTitle(".:: "+name+" console ::.");

            JPanel panel = new JPanel();
            setContentPane(panel);
            panel.setLayout(new GridLayout(0,columns));
            
            for (int l=0;l<lines;l++) {
                for (int c=0; c<columns;c++) {
                    text[l][c] = new JTextArea(1,1);
                    text[l][c].setFont(new Font("Courier", Font.PLAIN, 40));
                    text[l][c].setText(" ");
                    text[l][c].setEditable(false);
                    panel.add(text[l][c]);
                }
            }
            
            pack();
            setLocation(n*40, n*80);
            setVisible(true);

            n++;
        }

        public void setChar(int line, int col, String c){
            SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                    text[line][col].setText(c);
                }
            });
        }

    }
}
