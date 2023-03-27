package jacamo.infra;

import jaca.CAgentArch;
import jason.infra.local.BaseLocalMAS;
import jason.infra.local.LocalRuntimeServices;

/** This class implements the JaCaMo version of the runtime services. */
public class JaCaMoRuntimeServices extends LocalRuntimeServices {

    //private static Logger logger = Logger.getLogger(JaCaMoRuntimeServices.class.getName());

    public JaCaMoRuntimeServices(BaseLocalMAS masRunner) {
        super(masRunner);

        // register jacamo archs
        registerDefaultAgArch(CAgentArch.class.getName());
        registerDefaultAgArch(JaCaMoAgArch.class.getName());
    }
}
