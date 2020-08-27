package ia;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import cartago.ArtifactId;
import jaca.CAgentArch;
import jacamo.infra.JaCaMoAgArch;
import jason.architecture.AgArch;
import jason.architecture.MindInspectorAgArch;
import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.Intention;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
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
            CAgentArch        cartagoArch = null;
            MindInspectorAgArch mindInspArch = null;
            
            AgArch arch =  ag.getTS().getAgArch();
            while (arch != null) {
                if (arch instanceof CentralisedAgArch)
                    carch = (CentralisedAgArch)arch;
                if (arch instanceof CAgentArch)
                    cartagoArch = (CAgentArch)arch;
                if (arch instanceof MindInspectorAgArch) {
                    mindInspArch = (MindInspectorAgArch)arch;
                }
                arch = arch.getNextAgArch();                
            }

            carch.setAgName(agName);
            carch.getTS().setLogger(carch);
            carch.setLogger();
            ag.setLogger(carch);
            ag.initAg(); // to add it into web inspector

            // manage cartago stuff
            if (cartagoArch != null) {
                cartagoArch.init();

                // remove from BB all from cartago
                //ag.abolish(ASSyntax.parseLiteral("focused(_,_,_)"), null);
                ag.abolish(ASSyntax.parseLiteral("_[percept_type(obs_prop)]"), null);
                
                // re-focus
                ListTerm lart = new ListTermImpl();
                for (ArtifactId aid: cartagoArch.getFocusedArtsBeforeSerialization()) {
                    if (!aid.getName().startsWith("body_") && !aid.getName().startsWith("session_")) {
                        Literal art = ASSyntax.createLiteral("art_env",
                                ASSyntax.createAtom(aid.getWorkspaceId().getName()),  // workspace
                                ASSyntax.createAtom(aid.getName()), // art
                                ASSyntax.parseTerm("default")); // TODO: store and reuse namespace
                        System.out.println("   >> re focus on "+art);
                        lart.add(art);
                    }
                }
                Intention i = new Intention();
                i.setAtomic(1); // force this event to be selected first
                ag.getTS().getC().addAchvGoal( ASSyntax.createLiteral(JaCaMoAgArch.jcmAtom, "focus_env_art", lart, ASSyntax.createNumber(5)), i);
            }
            
            if (mindInspArch != null) {
                mindInspArch.init();
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
