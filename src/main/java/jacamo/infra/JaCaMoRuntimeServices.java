package jacamo.infra;

import jason.infra.centralised.CentralisedRuntimeServices;
import jason.infra.centralised.RunCentralisedMAS;

/** This class implements the JaCaMo version of the runtime services. */
public class JaCaMoRuntimeServices extends CentralisedRuntimeServices {

    //private static Logger logger = Logger.getLogger(JaCaMoRuntimeServices.class.getName());
    
    public JaCaMoRuntimeServices(RunCentralisedMAS masRunner) {
        super(masRunner);
    }

    /*
    @Override
    protected CentralisedAgArch newAgInstance() {
        return new JaCaMoAgArch();
    }
    */
}
