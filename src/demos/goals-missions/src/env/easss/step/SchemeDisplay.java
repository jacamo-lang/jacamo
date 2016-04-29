package easss.step;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SchemeDisplay extends JFrame {		
	

	public JButton goalAchieved, commitMission, leaveMission;
	public JTextField missionToCommit, missionToLeave, goalToAchieve;
	public JTextField schemeName;
	
	public SchemeDisplay(String title){
		setTitle(".:: "+title+" SCHEME-MANAGEMENT CONSOLE ::.");
		setSize(400,400);
		
		JPanel panel = new JPanel();
		setContentPane(panel);
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

		JPanel p1 = new JPanel();
		p1.add(new JLabel("Scheme name"));
		schemeName = new JTextField(10);
		p1.add(schemeName);
		panel.add(p1);
		
		JPanel p2 = new JPanel();
		commitMission = new JButton("commitMission");
		missionToCommit = new JTextField(10);
		p2.add(commitMission);
		p2.add(missionToCommit);
		panel.add(p2);
		
		JPanel p3 = new JPanel();
		leaveMission = new JButton("leaveMission");
		missionToLeave = new JTextField(10);
		p3.add(leaveMission);
		p3.add(missionToLeave);
		panel.add(p3);

		JPanel p4 = new JPanel();
		goalAchieved = new JButton("goalAchieved");
		goalToAchieve = new JTextField(10);
		p4.add(goalAchieved);
		p4.add(goalToAchieve);
		panel.add(p4);

	}
	
}
