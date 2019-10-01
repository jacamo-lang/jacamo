package comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

import jason.JasonException;
import jason.architecture.AgArch;
import jason.asSemantics.Circumstance;
import jason.asSemantics.Message;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;

public class FileComm extends AgArch {
    
    Random r     = new Random();
    File   myDir = null;
    
    @Override
    public void init() throws Exception {
        myDir = new File(getAgName());
        myDir.mkdirs();
        
        // creates a thread the wakes up the agent when there is a file in its directory
        new Thread() {
            public void run() {
                while (isRunning()) { // while the agent is running
                    if (myDir.list().length > 0) {
                        wake();
                    }
                    try {
                        sleep(300);
                    } catch (InterruptedException e) {}
                }
            }; 
        }.start();
    }
    
    @Override
    public void sendMsg(Message m) throws Exception {
        // randomly produces an exception (just to illustrate error handling)
        if (r.nextBoolean())
            throw new JasonException("Error sending "+m);
        
        try {
            // creates a file in receiver directory with the message
            //System.out.println("Senging message "+m);
            BufferedWriter out = new BufferedWriter(new FileWriter(m.getReceiver()+"/"+m.getMsgId()));
            out.append(m.getSender()+"\n");
            out.append(m.getIlForce()+"\n");
            out.append(m.getPropCont().toString()+"\n");
            out.close();
        } catch (Exception e) {
            throw new JasonException("Error sending "+m, e);
        }       
    }
    
    @Override
    public void checkMail() {
        Circumstance C = getTS().getC();
        
        // read files in my directory
        for (String fn: myDir.list()) {
            File f = new File(myDir+"/"+fn);
            try {
                BufferedReader in = new BufferedReader(new FileReader(f));
                String from = in.readLine();
                String ilf  = in.readLine();
                Literal content = ASSyntax.parseLiteral(in.readLine());
                C.addMsg( new Message(ilf, from, getAgName(), content) );
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            f.delete();         
        }
    }
}
