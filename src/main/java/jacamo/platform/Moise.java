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
import cartago.OpFeedbackParam;
import cartago.Workspace;
import jacamo.project.JaCaMoGroupParameters;
import jacamo.project.JaCaMoOrgParameters;
import jacamo.project.JaCaMoSchemeParameters;

public class Moise extends DefaultPlatformImpl {
    

    Logger logger = Logger.getLogger(Moise.class.getName());

    @Override
    public void init(String[] args) throws CartagoException {
    }
    
    @Override
    public void start() {
        cartago.CartagoEnvironment cenv = cartago.CartagoEnvironment.getInstance(); 
        Workspace main = cenv.getRootWSP().getWorkspace();
        
        for (JaCaMoOrgParameters o: project.getOrgs()) {
            try {
                // fix path for org
                o.addParameter("source", project.getOrgPaths().fixPath(o.getParameter("source")));

                // TODO: Just checking if the org wks exists is not a good solution, it must check if is already joined
                if (main.getChildWSP(o.getName()).equals(Optional.empty())) {
                    Workspace currentWks;
                    ICartagoContext context;                    
                    ArtifactId aid;
                    String boardClass;
                    
                    if (o.hasInstitution()) {
                        /*WorkspaceId instWid = cartagoCtx.joinWorkspace(o.getInstitution()); //, new AgentIdCredential("JaCaMoLauncherAgInst"));
                        ArtifactId instAId = cartagoCtx.lookupArtifact(instWid, o.getInstitution()+"_art");
                        aid = cartagoCtx.makeArtifact(wid, o.getName(), "sai.bridges.jacamo.OrgBoardSai", new Object[] { o.getParameter("source") } );
                        cartagoCtx.doAction(aid, new Op("setInstitution", new Object[] { o.getInstitution(), instAId } ));
                        */
                        //currentWks = main.getChildWSP(o.getInstitution()).get().getWorkspace();
                        boardClass = "sai.bridges.jacamo.OrgBoardSai";
                    } else {
                        boardClass = "ora4mas.nopl.OrgBoard";
                    }
                    currentWks = main.createWorkspace(o.getName()).getWorkspace();
                    context = currentWks.joinWorkspace(new AgentIdCredential("JaCaMoLauncherAgOrg"), new ICartagoCallback() {
                        public void notifyCartagoEvent(CartagoEvent a) {    }
                    });
                    aid = currentWks.makeArtifact(
                            context.getAgentId(), 
                            o.getName(), 
                            boardClass, 
                            new ArtifactConfig( new Object[] { o.getParameter("source") } ));
                    o.setWId(currentWks.getId());
                    if (o.hasInstitution()) {
                        ArtifactId instAId = currentWks.getArtifact(o.getInstitution()+"_art");
                        context.doAction(1, aid.getName(), new Op("setInstitution", new Object[] { o.getInstitution(), instAId } ), null, -1);
                        logger.info("OrgBoard(SAI) "+o.getName()+" created.");
                    } else {
                        logger.info("OrgBoard "+o.getName()+" created.");
                    }

                    // schemes
                    for (JaCaMoSchemeParameters s: o.getSchemes()) {
                        createScheme(aid, s, o, context);
                    }

                    // groups
                    for (JaCaMoGroupParameters g: o.getGroups()) {
                        createGroup(aid,null,g,o, context);
                    }
                    //EnvironmentWebInspector.get().registerWorkspace(o.getName());
                } else {
                    logger.info("Workspace "+o.getName()+" already exists, organisation will not be created.");
                }
            } catch (CartagoException e) {
                e.printStackTrace();
            }
        }
    }

    protected void createGroup(ArtifactId orgB, JaCaMoGroupParameters parent, JaCaMoGroupParameters g, JaCaMoOrgParameters org, ICartagoContext cartagoCtx) {
        String m = g.getName()+": "+g.getType()+" using artifact ora4mas.nopl.GroupBoard";

        try {
            OpFeedbackParam<ArtifactId> fb = new OpFeedbackParam<>();
            cartagoCtx.doAction(1, orgB.getName(), new Op("createGroup", new Object[] { g.getName(), g.getType(), fb} ), null, -1);
            ArtifactId aid = fb.get();
            int i=0;
            while (aid == null && i++ < 30) {
                Thread.sleep(50);
                aid = fb.get();
            }
            if (aid == null) { 
                logger.warning("ERROR creating group "+m);
                return;
            }

            logger.info("group created: "+m);
            if (g.hasDebug())
                cartagoCtx.doAction(1, aid.getName(), new Op("debug", new Object[] { g.getDebugConf() } ), null, -1);

            if (parent != null) {
                cartagoCtx.doAction(1, aid.getName(), new Op("setParentGroup", new Object[] { parent.getName() } ), null, -1);
            }
            String owner = g.getParameter("owner");
            if (owner != null) {
                cartagoCtx.doAction(1, aid.getName(), new Op("setOwner", new Object[] { owner } ), null, -1);
            }
            for (JaCaMoGroupParameters sg: g.getSubGroups()) {
                createGroup(orgB, g, sg, org, cartagoCtx);
            }

            //String respFor = g.getParameter("responsible-for"); // should be done after subgroup creation
            //if (respFor != null) {
            for (String respFor: g.getResponsibleFor()) {
                if (org.getScheme(respFor) == null)
                    logger.warning("** The scheme "+respFor+" does not existis in "+org.getName()+" so the group "+g.getName()+" cannot be responsible for it!");
                cartagoCtx.doAction(1, aid.getName(), new Op("addSchemeWhenFormationOk", new Object[] { respFor } ), null, -1);
            }
        } catch (InterruptedException e) {
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error creating group "+m,e);
        }

    }

    protected void createScheme(ArtifactId orgB, JaCaMoSchemeParameters s, JaCaMoOrgParameters org, ICartagoContext cartagoCtx) {
        String m = s.getName()+": "+s.getType()+" using artifact SchemeBoard";

        try {
            OpFeedbackParam<ArtifactId> fb = new OpFeedbackParam<>();
            cartagoCtx.doAction(1, orgB.getName(), new Op("createScheme", new Object[] { s.getName(), s.getType(), fb} ), null, -1);
            ArtifactId aid = fb.get();
            int i = 0;
            while (aid == null && i++ < 30) {
                Thread.sleep(50);
                aid = fb.get();
            }
            if (aid == null) { 
                logger.warning("ERROR creating scheme "+m);
                return;
            }

            logger.info("scheme created: "+m);
            if (s.hasDebug())
                cartagoCtx.doAction(1, aid.getName(), new Op("debug", new Object[] { s.getDebugConf() } ), null, -1);

            String owner = s.getParameter("owner");
            if (owner != null) {
                cartagoCtx.doAction(1, aid.getName(), new Op("setOwner", new Object[] { owner } ), null, -1);
            }
        } catch (InterruptedException e) {
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error creating scheme "+m,e);
        }
    }
}
