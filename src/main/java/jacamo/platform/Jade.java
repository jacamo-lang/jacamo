package jacamo.platform;

import java.util.logging.Level;
import java.util.logging.Logger;

import jacamo.util.Config;
import jason.JasonException;
import jason.infra.jade.RunJadeMAS;

public class Jade extends DefaultPlatformImpl {
    
    Logger logger = Logger.getLogger(Jade.class.getName());
    RunJadeMAS rJADE = null;
    
    @Override
    public void init(String[] args) throws JasonException {
        rJADE = new RunJadeMAS();
        rJADE.createButtons();
        rJADE.addInitArgs(args);
        if (rJADE.startContainer()) {
            logger.info("Jade Container started.");
        } else {
            logger.log(Level.WARNING,"Error starting JADE container!");
            return;
        }

        if (project.hasPlatformParameter("jade", "-gui")) {
            Config.get().setProperty(Config.JADE_RMA, "false");
        }
        if (project.hasPlatformParameter("jade", "-sniffer")) {
            Config.get().setProperty(Config.JADE_SNIFFER, "false");
        }
        
        rJADE.createAgs();
    }
        
    @Override
    public void start() {
        rJADE.startAgs();
    }
    
}
