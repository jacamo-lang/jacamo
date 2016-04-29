package jacamo.infra;

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
import cartago.ArtifactObsProperty;
import cartago.CartagoException;
import cartago.CartagoWorkspace;

public class EnvironmentInspector {

    private static JFrame  frame;
    private static JTabbedPane allArtsPane;
    private static ScheduledThreadPoolExecutor updater = new ScheduledThreadPoolExecutor(1);
    private static int guiCount = 60;

    private static Map<String, JTextPane>        artsPane = new HashMap<String, JTextPane>();
    private static Map<String, CartagoWorkspace> artsWrps = new HashMap<String, CartagoWorkspace>();
    private static Map<String, String>           artsLast = new HashMap<String, String>();
        
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

    public static void addInGui(CartagoWorkspace wks, ArtifactId aId) {
        if (frame == null)
            initFrame();

        JTextPane txtOP  = new JTextPane();
        
        // state
        JPanel nsp = new JPanel(new BorderLayout());
        txtOP.setContentType("text/html");
        txtOP.setEditable(false);
        txtOP.setAutoscrolls(false);
        nsp.add(BorderLayout.CENTER, new JScrollPane(txtOP));

        String id = wks.getId().getName()+"."+aId.getName();
        //artsInfo.put(id, wks.getController().getArtifactInfo(aId.getName()));
        artsWrps.put(id, wks);
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
                CartagoWorkspace wks = artsWrps.get(k);
                String aname = k.substring(k.indexOf(".")+1);
                ArtifactInfo info = wks.getController().getArtifactInfo(aname);
                
                String sOut = getArtHtml(wks.getId().getName(), info, 5);
                
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

    public static String getArtHtml(String wId, ArtifactInfo info, int refreshInterval) {
        StringBuilder out = new StringBuilder("<html>");
        out.append("<head><meta http-equiv=\"refresh\" content=\""+refreshInterval+"\"></head>");
        out.append("<span style=\"color: red; font-family: arial\"><font size=\"+2\">");
        out.append("Inspection of artifact <b>"+info.getId().getName()+"</b> in workspace "+wId+"</font></span>"); 
        out.append("<table border=0 cellspacing=3 cellpadding=6 style='font-family:verdana'>");
        for (ArtifactObsProperty op: info.getObsProperties()) {
            out.append("<tr><td>"+op.getName()+"</td><td>"+op.getValue()+"</td></tr>");
        }
        out.append("</table>");
        /*out.append("</br>Operations:<ul>");
        for (OperationInfo op: info.getOngoingOp()) {
            out.append("<li>"+op.toString()+"</li>");
        }
        for (OpDescriptor op: info.getOperations()) {
            out.append("<li>"+op.getOp().getName()+"</li>");
        }
        out.append("</ul>");*/
        out.append("</html>");
        return out.toString();
    }
}
