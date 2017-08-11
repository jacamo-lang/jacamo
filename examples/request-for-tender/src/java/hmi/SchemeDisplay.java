package hmi;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SchemeDisplay extends JFrame {


    public JButton focuson, unfocuson, goalAchieved, commitMission, leaveMission, createScheme, deleteScheme;
    public JTextField schemeToFocusOn, schemeToUnfocus, schemeToDelete, schemeToCreate, missionToCommit, missionToLeave, goalToAchieve;
    public JTextField schemeName;


    public SchemeDisplay(String title, String sc){
        setTitle(".:: "+title+" SCHEME-MANAGEMENT CONSOLE ::.");
        setSize(400,400);

        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        JPanel p1 = new JPanel();
        p1.add(new JLabel("Scheme name"));
        schemeName = new JTextField(10);
        schemeName.setText(sc);
        p1.add(schemeName);
        panel.add(p1);

        JPanel p2bis = new JPanel();
        createScheme = new JButton("createScheme of type");
        schemeToCreate = new JTextField(10);
        p2bis.add(createScheme);
        p2bis.add(schemeToCreate);
        panel.add(p2bis);

        JPanel p3bis = new JPanel();
        deleteScheme = new JButton("deleteScheme of name");
        schemeToDelete = new JTextField(10);
        p3bis.add(deleteScheme);
        p3bis.add(schemeToDelete);
        panel.add(p3bis);

        JPanel p2focus = new JPanel();
        focuson = new JButton("focus on");
        schemeToFocusOn = new JTextField(10);
        p2focus.add(focuson);
        p2focus.add(schemeToFocusOn);
        panel.add(p2focus);

        JPanel p2unfocus = new JPanel();
        unfocuson = new JButton("unfocus of");
        schemeToUnfocus = new JTextField(10);
        p2unfocus.add(unfocuson);
        p2unfocus.add(schemeToUnfocus);
        panel.add(p2unfocus);

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
