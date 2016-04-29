package easss.step;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Display extends JFrame {		
	
	private JTextArea text;
	private JLabel numMsg;
	
	public Display(){
		setTitle(".:: ENVIRONMENT CONSOLE ::.");
		setSize(600,600);
		
		JPanel panel = new JPanel();
		setContentPane(panel);
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		
		numMsg = new JLabel("0");
		text = new JTextArea();
		text.setMinimumSize(new Dimension(580,550));
		text.setMaximumSize(new Dimension(580,550));
		text.setPreferredSize(new Dimension(580,550));
		text.setEditable(true);

		panel.add(text);
		panel.add(Box.createVerticalStrut(5));
		panel.add(numMsg);
	}
	
	public void addText(final String s){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				text.append(s+"\n");
			}
		});
	}
	
	public void updateNumMsgField(final int value){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				numMsg.setText(""+value);
			}
		});
	}
}
