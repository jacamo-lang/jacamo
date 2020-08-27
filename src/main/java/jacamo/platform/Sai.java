package jacamo.platform;

import java.util.logging.Logger;

import cartago.AgentIdCredential;
import cartago.ArtifactConfig;
import cartago.ArtifactId;
import cartago.CartagoEvent;
import cartago.CartagoException;
import cartago.ICartagoCallback;
import cartago.ICartagoContext;
import cartago.Op;
import cartago.OpFeedbackParam;
import cartago.Workspace;
import jacamo.project.JaCaMoInstParameters;

public class Sai extends DefaultPlatformImpl {
    
    protected ArtifactId          saiArtId = null;
    
    Logger logger = Logger.getLogger(Sai.class.getName());

    @Override
    public void init(String[] args) throws CartagoException {
    }
    
    @Override
    public void start() {
        cartago.CartagoEnvironment cenv = cartago.CartagoEnvironment.getInstance(); 
        Workspace main = cenv.getRootWSP().getWorkspace();
        
        for (JaCaMoInstParameters inst: project.getInstitutions()) {
            try {
                // fix path for org
                String file = project.getOrgPaths().fixPath(inst.getParameter("source"));
                if (file.startsWith("file:"))
                    file = file.substring(5);
                inst.addParameter("source", file);

                // create the SAI workspace
                Workspace currentWks = main.createWorkspace(inst.getName()).getWorkspace();

                ICartagoContext context = currentWks.joinWorkspace(new AgentIdCredential("JaCaMoLauncherAgInst"), new ICartagoCallback() {
                    public void notifyCartagoEvent(CartagoEvent a) {    }
                });
                inst.setWId(currentWks.getId());

                // create the sai artifact
                saiArtId = currentWks.makeArtifact(
                        context.getAgentId(), 
                        inst.getName()+"_art", 
                        "sai.bridges.jacamo.ConstitutiveArt", 
                        new ArtifactConfig( new Object[] { inst.getName(), inst.getParameter("source") } ));
                
                // get listener object from SAI                
                OpFeedbackParam<Object> fbre = new OpFeedbackParam<>();
                context.doAction(1, saiArtId.getName(), new Op("getRuleEngine", new Object[] { fbre } ), null, -1);
                Object fbrer = fbre.get();
                // wait for completion
                int i=0;
                while (fbrer == null && i++ < 30) {
                    Thread.sleep(50);
                    fbrer = fbre.get();
                }
                if (fbrer == null) {
                    logger.warning("ERROR getting getRuleEngine ");
                } else {
                    inst.setRE(fbrer);
                }

                logger.info("Institutional Workspace "+inst.getName()+" created.");

                //EnvironmentWebInspector.get().registerWorkspace(inst.getName());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
