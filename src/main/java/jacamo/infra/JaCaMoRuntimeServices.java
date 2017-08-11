package jacamo.infra;

import java.util.ArrayList;
import java.util.List;

import jaca.CAgentArch;
import jason.asSemantics.Agent;
import jason.infra.centralised.BaseCentralisedMAS;
import jason.infra.centralised.CentralisedRuntimeServices;
import jason.mas2j.ClassParameters;
import jason.runtime.Settings;

/** This class implements the JaCaMo version of the runtime services. */
public class JaCaMoRuntimeServices extends CentralisedRuntimeServices {

    //private static Logger logger = Logger.getLogger(JaCaMoRuntimeServices.class.getName());

    public JaCaMoRuntimeServices(BaseCentralisedMAS masRunner) {
        super(masRunner);
    }

    @Override
    public String createAgent(String agName, String agSource, String agClass, List<String> archClasses, ClassParameters bbPars, Settings stts, Agent father) throws Exception {
        if (archClasses == null)
            archClasses = new ArrayList<String>();

        if (!archClasses.contains(JaCaMoAgArch.class.getName()))
            archClasses.add(JaCaMoAgArch.class.getName());
        if (!archClasses.contains(CAgentArch.class.getName()))
            archClasses.add(CAgentArch.class.getName());

        return super.createAgent(agName, agSource, agClass, archClasses, bbPars, stts, father);
    }

    /*
    @Override
    protected CentralisedAgArch newAgInstance() {
        return new JaCaMoAgArch();
    }
    */
}
