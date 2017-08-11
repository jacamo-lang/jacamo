package hmi;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class RFTDisplay extends JFrame {


    public JButton addRFT;
    public JTextField rftToAdd, conditionToAdd, deadlineToAdd;

    public JTextArea text;

    public RFTDisplay(String title){
        setTitle(".:: "+title+" RFT-MANAGEMENT CONSOLE ::.");
        setSize(400,400);

        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

    /*  JPanel textpanel = new JPanel();
        text = new JTextArea();
        text.setMinimumSize(new Dimension(350,40));
        text.setMaximumSize(new Dimension(350,40));
        text.setPreferredSize(new Dimension(350,40));
        // text.setEditable(true);
        // text.setAutoscrolls(true);
        textpanel.add(text);
        // panel.add(Box.createVerticalStrut(5));
        panel.add(textpanel);*/

        JPanel rftPanel = new JPanel();
        addRFT = new JButton("add RFT");
        rftToAdd = new JTextField(10);
        conditionToAdd = new JTextField(10);
        deadlineToAdd = new JTextField(10);
        rftPanel.add(rftToAdd);
        rftPanel.add(conditionToAdd);
        rftPanel.add(deadlineToAdd);
        rftPanel.add(addRFT);
        panel.add(rftPanel);
    }

    /*public void addText(final String s){
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
//              text.append(s+"\n");
                text.setText(s);
            }
        });
    }*/

}
