package jacamo.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cartago.AgentIdCredential;
import cartago.ArtifactId;
import cartago.CartagoContext;
import cartago.CartagoException;
import cartago.CartagoService;
import cartago.Op;
import cartago.WorkspaceId;
import cartago.WorkspaceKernel;
import jaca.CartagoEnvironment;
import jacamo.project.JaCaMoInstParameters;
import jacamo.project.JaCaMoWorkspaceParameters;
import jason.mas2j.ClassParameters;

public class Cartago extends DefaultPlatformImpl {
    
    protected CartagoEnvironment        env;
    protected CartagoContext            cartagoCtx;

    Logger logger = Logger.getLogger(Cartago.class.getName());

    @Override
    public void init(String[] args) throws CartagoException {
        env = new CartagoEnvironment();
        env.init( args );
        cartagoCtx = CartagoService.startSession(CartagoService.MAIN_WSP_NAME, new AgentIdCredential("JaCaMo_Launcher"));
    }
    
    @Override
    public void start() {
        for (JaCaMoWorkspaceParameters wp : project.getWorkspaces()) {
            try {
                if (project.isInDeployment(wp.getNode())) {

                    if (!CartagoService.getNode().getWorkspaces().contains(wp.getName())) {
                        CartagoService.createWorkspace(wp.getName());
                        logger.info("Workspace " + wp.getName() + " created.");
                        EnvironmentWebInspector.get().registerWorkspace(wp.getName());
                    } else {
                        logger.info("Workspace " + wp.getName() + " already exists.");
                    }

                    logger.info("getJoinedWorkspaces: " + cartagoCtx.getJoinedWorkspaces());
                    WorkspaceId wid = null;

                    WorkspaceKernel kernel = CartagoService.getNode().getWorkspace(wp.getName()).getKernel();
                    List<String> ids = new ArrayList<>();
                    for (ArtifactId aid : kernel.getArtifactIdList())
                        ids.add(aid.getName());

                    // getCurrentAgentContexts()
                    for (WorkspaceId w : cartagoCtx.getJoinedWorkspaces()) {
                        if (w.getName().equals(wp.getName())) {
                            wid = cartagoCtx.getJoinedWspId(wp.getName());
                        }
                    }
                    if (wid == null)
                        wid = cartagoCtx.joinWorkspace(wp.getName(), new AgentIdCredential("JaCaMoLauncherAgEnv"));

                    wp.setWId(wid);

                    // check institution
                    for (JaCaMoInstParameters inst : project.getInstitutions()) {
                        if (inst.hasWorkspace(wp.getName()) && inst.getRE() != null) {
                            cartagoCtx.doAction(wid, new Op("setWSPRuleEngine", new Object[] { inst.getRE() }));
                        }
                    }

                    // create artifacts
                    for (String aName : wp.getArtifacts().keySet()) {
                        String m = null;
                        try {
                            if (!artifactExists(wp.getName(), aName)) {
                                ClassParameters cp = wp.getArtifacts().get(aName);
                                m = "artifact " + aName + ": " + cp.getClassName() + "(" + cp.getParametersStr(",")
                                        + ") at " + wp.getName();
                                ArtifactId aid = cartagoCtx.makeArtifact(wid, aName, cp.getClassName(),
                                        cp.getTypedParametersArray());
                                // artIds.put(aName, aid);
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
                        CartagoService.enableDebug(wp.getName());
                    }
                }
            } catch (CartagoException e) {
                logger.log(Level.SEVERE, "error creating environmet, workspace:" + wp.getName(), e);
            }
        }
    }
    
    @Override
    public void stop() {
        try {
            CartagoService.shutdownNode();
        } catch (CartagoException e) {
            e.printStackTrace();
        }
    }
    
    public boolean artifactExists(String wksName, String artName) throws CartagoException {
        for (ArtifactId aid : CartagoService.getController(wksName).getCurrentArtifacts()) {
            if (aid.getName().equals(artName)) return true;
        }
        return false;
    }
}
