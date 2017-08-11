package hmi;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GroupDisplay extends JFrame {


    public JButton addScheme, removeScheme, setOwner, adoptRole, leaveRole, focuson, unfocuson, createSubGroup, deleteSubGroup;
    public JTextField groupToFocusOn, groupToUnfocus, roleToAdopt, roleToLeave, schemeToAdd, schemeToRemove, owner, subGroupToCreate, subGroupToDelete;
    public JTextField groupName;
    public JTextArea text;

    public GroupDisplay(String title, String gr){
        setTitle(".:: "+title+" GROUP-MANAGEMENT CONSOLE ::.");
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

        JPanel p1 = new JPanel();
        p1.add(new JLabel("Group name"));
        groupName = new JTextField(10);
        groupName.setText(gr);
        p1.add(groupName);
        panel.add(p1);

        JPanel p2 = new JPanel();
        adoptRole = new JButton("adoptRole");
        roleToAdopt = new JTextField(10);
        p2.add(adoptRole);
        p2.add(roleToAdopt);
        panel.add(p2);

        JPanel p3 = new JPanel();
        leaveRole = new JButton("leaveRole");
        roleToLeave = new JTextField(10);
        p3.add(leaveRole);
        p3.add(roleToLeave);
        panel.add(p3);

        JPanel p2bis = new JPanel();
        createSubGroup = new JButton("createSubGroup of type");
        subGroupToCreate = new JTextField(10);
        p2bis.add(createSubGroup);
        p2bis.add(subGroupToCreate);
        panel.add(p2bis);

        JPanel p3bis = new JPanel();
        deleteSubGroup = new JButton("deleteSubGroup of name");
        subGroupToDelete = new JTextField(10);
        p3bis.add(deleteSubGroup);
        p3bis.add(subGroupToDelete);
        panel.add(p3bis);

        JPanel p2focus = new JPanel();
        focuson = new JButton("focus on");
        groupToFocusOn = new JTextField(10);
        p2focus.add(focuson);
        p2focus.add(groupToFocusOn);
        panel.add(p2focus);

        JPanel p2unfocus = new JPanel();
        unfocuson = new JButton("unfocus of");
        groupToUnfocus = new JTextField(10);
        p2unfocus.add(unfocuson);
        p2unfocus.add(groupToUnfocus);
        panel.add(p2unfocus);

        JPanel p4 = new JPanel();
        addScheme = new JButton("addScheme");
        schemeToAdd = new JTextField(10);
        p4.add(addScheme);
        p4.add(schemeToAdd);
        panel.add(p4);

        JPanel p5 = new JPanel();
        removeScheme = new JButton("removeScheme");
        schemeToRemove = new JTextField(10);
        p5.add(removeScheme);
        p5.add(schemeToRemove);
        panel.add(p5);

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
