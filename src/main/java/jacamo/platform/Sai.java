package jacamo.platform;

import java.util.logging.Logger;

import cartago.AgentIdCredential;
import cartago.ArtifactId;
import cartago.CartagoContext;
import cartago.CartagoException;
import cartago.CartagoService;
import cartago.Op;
import cartago.OpFeedbackParam;
import cartago.WorkspaceId;
import jacamo.project.JaCaMoInstParameters;
import sai.bridges.jacamo.ConstitutiveArt;
import sai.bridges.jacamo.RuleEngine;

public class Sai extends DefaultPlatformImpl {
    
    protected CartagoContext      cartagoCtx;
    protected ArtifactId          saiArtId = null;
    
    Logger logger = Logger.getLogger(Sai.class.getName());

    @Override
    public void init(String[] args) throws CartagoException {
        cartagoCtx = CartagoService.startSession(CartagoService.MAIN_WSP_NAME, new AgentIdCredential("JaCaMo_Inst_Launcher"));
    }
    
    @Override
    public void start() {
        for (JaCaMoInstParameters inst: project.getInstitutions()) {
            try {
                // fix path for org
                inst.addParameter("source", project.getOrgPaths().fixPath(inst.getParameter("source")));

                CartagoService.createWorkspace(inst.getName());
                logger.info("Workspace "+inst.getName()+" created.");

                WorkspaceId wid = cartagoCtx.joinWorkspace(inst.getName(), new AgentIdCredential("JaCaMoLauncherAgInst"));

                // TODO: what is the first arf for const art?
                saiArtId = cartagoCtx.makeArtifact(
                        wid, inst.getName(), 
                        ConstitutiveArt.class.getName(), 
                        new Object[] { inst.getName(), inst.getParameter("source") } );
                
                // get listener object from SAI
                OpFeedbackParam<RuleEngine> fbre = new OpFeedbackParam<>();
                cartagoCtx.doAction(saiArtId, new Op("getRuleEngine", new Object[] { fbre} ));
                System.out.println("=1"+fbre.get());

                // add listeners in all worksapce
                for (String wn: inst.getWorkspaces()) {
                    wid = cartagoCtx.joinWorkspace(wn, new AgentIdCredential("JaCaMoLauncherAgInst"));
                    //wid = cartagoCtx.getJoinedWspId(wn);
                    //wid = project.getWorkspace(wn).getWId();
                    //System.out.println(wn+" wid = "+wid);
                    cartagoCtx.doAction(wid, new Op("setWSPRuleEngine", new Object[] {fbre.get()} ));
                    System.out.println("=2="+wn);
                     
                }

            } catch (CartagoException e) {
                e.printStackTrace();
            }
        }
    }

}
