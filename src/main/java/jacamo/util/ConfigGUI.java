package jacamo.util;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

public class ConfigGUI {

    protected JTextField jacamoTF;
    protected JTextField javaTF;
    //protected JTextField dotTF;
    //protected JCheckBox  alsoForJasonCB;

    protected String initJacamoHome = null;

    protected static Config userProperties = Config.get();

    public static void main(String[] args) {
        new ConfigGUI().run();
    }

    protected static ConfigGUI getNewInstance() {
        return new ConfigGUI();
    }

    public void run() {
        userProperties.resetSomeProps();
        userProperties.fix();
        final ConfigGUI jid = getNewInstance();
        JFrame f = null;
        try {
            f = new JFrame(jid.getWindowTitle());
        } catch (Exception e) {
            // uses console
            userProperties.store();
            System.out.println("\nYou can edit the file "+userProperties.getUserConfFile()+" for your local configuration.");
            return;
        }
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel pBt = new JPanel(new FlowLayout());
        //jid.alsoForJasonCB = new JCheckBox("save this configuration also for Jason");
        //jid.alsoForJasonCB.setSelected(true);
        //pBt.add(jid.alsoForJasonCB);

        JButton bQuit = new JButton("Exit without saving");
        bQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        pBt.add(bQuit);

        JButton bSave = new JButton("Save configuration and Exit");
        bSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                jid.save();
                System.exit(0);
            }
        });
        pBt.add(bSave);

        JPanel p = new JPanel(new BorderLayout());
        p.add(BorderLayout.CENTER, jid.getJasonConfigPanel());
        p.add(BorderLayout.SOUTH, pBt);

        f.getContentPane().add(p);
        f.pack();
        f.setVisible(true);
    }

    protected String getWindowTitle() {
        return "JaCaMo Configuration -- "+userProperties.getJaCaMoVersion();
    }

    public JPanel getJasonConfigPanel() {
        JPanel pop = new JPanel();
        pop.setLayout(new BoxLayout(pop, BoxLayout.Y_AXIS));

        // jacamo home
        jacamoTF = new JTextField(35);
        JPanel jacamoHomePanel = new JPanel(new GridLayout(0,1));
        jacamoHomePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(), "JaCaMo", TitledBorder.LEFT, TitledBorder.TOP));
        JPanel jacamoJarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jacamoJarPanel.add(new JLabel("jacamo.jar location"));
        jacamoJarPanel.add(jacamoTF);
        jacamoJarPanel.add(createBrowseButton("jacamo.jar", jacamoTF));
        jacamoHomePanel.add(jacamoJarPanel);

        pop.add(jacamoHomePanel);


        // java home
        JPanel javaHomePanel = new JPanel();
        javaHomePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(), "Java Home", TitledBorder.LEFT, TitledBorder.TOP));
        javaHomePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        javaHomePanel.add(new JLabel("Directory"));
        javaTF = new JTextField(35);
        javaHomePanel.add(javaTF);
        JButton setJava = new JButton("Browse");
        setJava.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
                     chooser.setDialogTitle("Select the Java JDK home directory");
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        String javaHome = (new File(chooser.getSelectedFile().getPath())).getCanonicalPath();
                        if (Config.checkJavaHomePath(javaHome)) {
                            javaTF.setText(javaHome);
                        } else {
                            JOptionPane.showMessageDialog(null, "The selected JDK home directory doesn't have the bin/javac file!");
                        }
                    }
                } catch (Exception e) {}
            }
        });
        javaHomePanel.add(setJava);
        pop.add(javaHomePanel);

        // dot path
        //dotTF = new JTextField(35);
        //dotTF.setToolTipText("Dot is used to produce graphical representation of the organization. It can be installed from http://www.graphviz.org.");
        JPanel dotHomePanel = new JPanel(new GridLayout(0,1));
        dotHomePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(), "dot (optional)", TitledBorder.LEFT, TitledBorder.TOP));
        
        /*JPanel dotPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dotPanel.add(new JLabel("program location"));
        dotPanel.add(dotTF);
        dotHomePanel.add(dotPanel);

        pop.add(dotHomePanel);*/

        initJacamoHome = userProperties.getProperty(Config.JACAMO_JAR);
        jacamoTF.setText(initJacamoHome);

        javaTF.setText(userProperties.getJavaHome());
        /*String v = userProperties.getDotPath();
        if (v == null) v = "";
        dotTF.setText(v);*/
        return pop;
    }

    protected JButton createBrowseButton(final String jarfile, final JTextField tf) {
        JButton bt = new JButton("Browse");
        bt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
                    chooser.setDialogTitle("Select the "+jarfile+" file");
                    chooser.setFileFilter(new JarFileFilter("The "+jarfile+" file"));
                    //chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        String selJar = (new File(chooser.getSelectedFile().getPath())).getCanonicalPath();
                        if (Config.checkJar(selJar)) {
                            tf.setText(selJar);
                        } else {
                            JOptionPane.showMessageDialog(null, "The selected "+jarfile+" file was not ok!");
                        }
                    }
                } catch (Exception e) {}
            }
        });
        return bt;
    }

    public void save() {
        if (Config.checkJar(jacamoTF.getText())) {
            String jh = jacamoTF.getText().trim();
            userProperties.put(Config.JACAMO_JAR, jh);
            if ( !jh.equals(initJacamoHome)) {
                // change other paths accordingly
                userProperties.resetSomeProps();
                userProperties.fix();
            }
        }

        if (Config.checkJavaHomePath(javaTF.getText()) || Config.checkJREHomePath(javaTF.getText())) {
            userProperties.setJavaHome(javaTF.getText().trim());
        }

        /*File fDot = new File(dotTF.getText().trim());
        userProperties.put(Config.DOT_PATH, fDot.getAbsolutePath());
        if (!fDot.exists())
            System.out.println("The dot program does not exist at "+fDot);
        */
        
        userProperties.store();

        // also store Jason conf if not installed
        /*
        File jasonConf = jason.jeditplugin.Config.get().getUserConfFile();
        if (!jasonConf.exists() || alsoForJasonCB.isSelected()) {
            userProperties.store(jasonConf);
        }
        */
    }

    class JarFileFilter extends FileFilter {
        String ds;
        public JarFileFilter(String ds) {
            this.ds  = ds;
        }
        public boolean accept(File f) {
            if (f.getName().endsWith("jar") || f.isDirectory()) {
                return true;
            } else {
                return false;
            }
        }

        public String getDescription() {
            return ds;
        }
    }
}
