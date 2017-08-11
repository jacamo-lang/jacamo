package hmi;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class MsgDisplay extends JFrame {

    private JTextArea text;

    public MsgDisplay(String name){
        setTitle(name+" console");
        setSize(405,405);

        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        text = new JTextArea();
        text.setMinimumSize(new Dimension(400,400));
        text.setMaximumSize(new Dimension(400,400));
        text.setPreferredSize(new Dimension(400,400));
        text.setEditable(true);

        panel.add(text);
        panel.add(Box.createVerticalStrut(5));
    }

    public void addText(final String s){
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                text.append(s+"\n");
            }
        });
    }
}
