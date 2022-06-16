package jacamo.infra;

import jason.infra.local.BaseLocalMAS;
import jason.infra.local.LocalRuntimeServices;

/** This class implements the JaCaMo version of the runtime services. */
public class JaCaMoRuntimeServices extends LocalRuntimeServices {

    //private static Logger logger = Logger.getLogger(JaCaMoRuntimeServices.class.getName());

    public JaCaMoRuntimeServices(BaseLocalMAS masRunner) {
        super(masRunner);
    }

    /*
    @Override
    public String createAgent(String agName, String agSource, String agClass, Collection<String> archClasses, ClassParameters bbPars, Settings stts, Agent father) throws Exception {
        
        if (father == null) {
            // if no father to copy ag archs
            //    adds jacamo and cartago archs
            
            if (archClasses == null)
                archClasses = new ArrayList<>();
    
            if (!archClasses.contains(JaCaMoAgArch.class.getName()))
                archClasses.add(JaCaMoAgArch.class.getName());
            if (!archClasses.contains(CAgentArch.class.getName()))
                archClasses.add(CAgentArch.class.getName());
        }
        
        return super.createAgent(agName, agSource, agClass, archClasses, bbPars, stts, father);
    }
    */
}
