package jacamo.platform;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import cartago.AgentIdCredential;
import cartago.ArtifactConfig;
import cartago.ArtifactId;
import cartago.CartagoEvent;
import cartago.CartagoException;
import cartago.ICartagoCallback;
import cartago.ICartagoContext;
import cartago.Op;
import cartago.Workspace;
import jaca.CartagoEnvironment;
import jacamo.project.JaCaMoInstParameters;
import jacamo.project.JaCaMoWorkspaceParameters;
import jason.mas2j.ClassParameters;

public class Cartago extends DefaultPlatformImpl {
    
    protected CartagoEnvironment        env;

    Logger logger = Logger.getLogger(Cartago.class.getName());

    @Override
    public void init(String[] args) throws CartagoException {
        env = new CartagoEnvironment();
        env.init( args );
    }
    
    @Override
    public void start() {
        cartago.CartagoEnvironment cenv = cartago.CartagoEnvironment.getInstance(); 
        Workspace main = cenv.getRootWSP().getWorkspace();
        for (JaCaMoWorkspaceParameters wp : project.getWorkspaces()) {
            try {
                Workspace currentWks = null;
                
                if (main.getChildWSP(wp.getName()).equals(Optional.empty())) {
                    currentWks = main.createWorkspace(wp.getName()).getWorkspace();
                    logger.info("Workspace " + wp.getName() + " created.");
                    EnvironmentWebInspector.get().registerWorkspace(currentWks.getId().getFullName());
                } else {
                    logger.info("Workspace " + wp.getName() + " already exists.");
                }

                ICartagoContext context = currentWks.joinWorkspace(new AgentIdCredential("JaCaMoLauncherAgEnv"), new ICartagoCallback() {
                    public void notifyCartagoEvent(CartagoEvent a) {    }
                });
                wp.setWId(currentWks.getId());

                // check institution
                for (JaCaMoInstParameters inst : project.getInstitutions()) {
                    if (inst.hasWorkspace(wp.getName()) && inst.getRE() != null) {
                        context.doAction(1, new Op("setWSPRuleEngine", new Object[] { inst.getRE() }), null, -1);  
                        logger.info("institution "+inst.getName()+" linked to "+wp.getName());
                    }
                }

                // create artifacts
                for (String aName : wp.getArtifacts().keySet()) {
                    String m = aName;
                    try {
                        if (currentWks.getArtifact(aName) == null) {
                            ClassParameters cp = wp.getArtifacts().get(aName);
                            m = "artifact " 
                                    + aName + ": " + cp.getClassName() + "(" + cp.getParametersStr(",")             
                                    + ") at " + wp.getName();
                            ArtifactId aid = currentWks
                                    .makeArtifact(
                                            context.getAgentId(),
                                            aName, 
                                            cp.getClassName(),
                                            new ArtifactConfig(
                                                    cp.getTypedParametersArray()));
                            logger.info(m + " created.");
                            if (wp.hasDebug())
                                EnvironmentInspector.addInGui(wp.getName(), aid);
                        } else {
                            logger.info("artifact" + aName + " already exists.");

                        }
                    } catch (CartagoException e) {
                        logger.log(Level.SEVERE, "error creating " + m, e);
                    }
                }
                if (wp.hasDebug()) {
                    cartago.CartagoEnvironment.getInstance().enableDebug(wp.getName());
                }
            } catch (CartagoException e) {
                logger.log(Level.SEVERE, "error creating environmet, workspace:" + wp.getName(), e);
            }
        }
    }
    
    @Override
    public void stop() {
        try {
            cartago.CartagoEnvironment.getInstance().shutdown();
        } catch (CartagoException e) {
            e.printStackTrace();
        }
    }
}
