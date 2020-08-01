package jacamo.platform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import cartago.ArtifactId;
import cartago.ArtifactInfo;
import cartago.CartagoEnvironment;
import cartago.CartagoException;

public class EnvironmentInspector {

    private static JFrame  frame;
    private static JTabbedPane allArtsPane;
    private static ScheduledThreadPoolExecutor updater = new ScheduledThreadPoolExecutor(1);
    private static int guiCount = 60;

    private static Map<String, JTextPane>        artsPane = new HashMap<>();
    private static Map<String, String>           artsWrps = new HashMap<>();
    private static Map<String, String>           artsLast = new HashMap<>();

    private static void initFrame() {
        frame = new JFrame("..:: Environment Inspector ::..");
        allArtsPane = new JTabbedPane(JTabbedPane.LEFT);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.add(BorderLayout.CENTER, allArtsPane);
        frame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        guiCount = guiCount+30;
        frame.setBounds(0, 0, 800, (int)(screenSize.height * 0.8));
        frame.setLocation(
                (screenSize.width  / 2)-guiCount - frame.getWidth() / 2,
                (screenSize.height / 2)+guiCount - frame.getHeight() / 2);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    public static void addInGui(String wksName, ArtifactId aId) {
        if (frame == null)
            initFrame();

        JTextPane txtOP  = new JTextPane();

        // state
        JPanel nsp = new JPanel(new BorderLayout());
        txtOP.setContentType("text/html");
        txtOP.setEditable(false);
        txtOP.setAutoscrolls(false);
        nsp.add(BorderLayout.CENTER, new JScrollPane(txtOP));

        String id = wksName+"."+aId.getName();
        //artsInfo.put(id, wks.getController().getArtifactInfo(aId.getName()));
        artsWrps.put(id, wksName);
        artsPane.put(id, txtOP);
        allArtsPane.add(id, nsp);

        updater.scheduleAtFixedRate(new Runnable() {
            public void run() {
                updateOP();
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    static void updateOP() {
        for (String k: artsPane.keySet()) {
            try {
                String wks = artsWrps.get(k);
                String aname = k.substring(k.indexOf(".")+1);
                ArtifactInfo info = CartagoEnvironment.getInstance().getController(wks).getArtifactInfo(aname);

                String sOut = EnvironmentWebInspector.getArtHtml(wks, info);

                String lastOut = artsLast.get(k);
                if (lastOut == null || !lastOut.endsWith(sOut)) {
                    artsPane.get(k).setText(sOut);
                }

                artsLast.put(k, sOut);
            } catch (CartagoException e) {
                e.printStackTrace();
            }
        }
    }

}
