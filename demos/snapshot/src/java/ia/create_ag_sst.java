package ia;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Set;

import cartago.Op;
import cartago.WorkspaceId;
import jaca.CAgentArch;
import jason.architecture.AgArch;
import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import jason.infra.centralised.CentralisedAgArch;
import jason.infra.centralised.RunCentralisedMAS;

@SuppressWarnings("serial")
public class create_ag_sst extends DefaultInternalAction {
    
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        String agName = args[0].toString();
        String fName = ((StringTerm)args[1]).getString();
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fName))) {
            
            Agent ag = (Agent)in.readObject();

            // find cent & cartago archs
            CentralisedAgArch carch = null;
            CAgentArch        cartagoArch = null;;
            AgArch arch =  ag.getTS().getUserAgArch().getFirstAgArch();
            while (arch != null) {
                if (arch instanceof CentralisedAgArch)
                    carch = (CentralisedAgArch)arch;
                if (arch instanceof CAgentArch)
                    cartagoArch = (CAgentArch)arch;
                arch = arch.getNextAgArch();                
            }

            //ag.getTS().getUserAgArch().setTS(ag.getTS());

            carch.setAgName(agName);
            carch.getTS().setLogger(carch);
            carch.setLogger();
            ag.setLogger(carch);
            ag.initAg(); // to add it into web inspector
            
            if (cartagoArch != null) {
                // remove focused and joined
                ts.getAg().abolish(ASSyntax.parseLiteral("joined(_,_)"), null);
                ts.getAg().abolish(ASSyntax.parseLiteral("focused(_,_,_)"), null);
                
                System.out.println(":::"+ts.getAg().getBB());
                // re-join
                Set<WorkspaceId> prevW = cartagoArch.getAllJoinedWsps();
                System.out.println("antes "+prevW);
                cartagoArch.init();
                System.out.println("depois "+cartagoArch.getAllJoinedWsps());
                for (WorkspaceId w: prevW) {
                    if (!w.getName().equals("main")) {
                        cartagoArch.getEnvSession().doAction(new Op("joinWorkspace", w.getName(), "K"), null, Long.MAX_VALUE);
                        System.err.println("ok for "+w.getName());
                    }
                }
                
                // re-focus
            }
            
            RunCentralisedMAS.getRunner().addAg(carch);
            
            // TODO: create a thread for the agent, ideally it should use the platform way to run the agent (pool, jade, ....)
            Thread agThread = new Thread(carch);
            carch.setThread(agThread);
            agThread.start();
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;        
    }
}
