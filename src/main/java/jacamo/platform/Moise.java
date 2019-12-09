package jacamo.platform;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cartago.AgentIdCredential;
import cartago.ArtifactId;
import cartago.CartagoContext;
import cartago.CartagoException;
import cartago.CartagoService;
import cartago.Op;
import cartago.OpFeedbackParam;
import cartago.WorkspaceId;
import jacamo.project.JaCaMoGroupParameters;
import jacamo.project.JaCaMoOrgParameters;
import jacamo.project.JaCaMoSchemeParameters;

public class Moise extends DefaultPlatformImpl {
    
    protected Map<String,CartagoContext> cartagoCtxs = new HashMap<>();

    Logger logger = Logger.getLogger(Moise.class.getName());

    @Override
    public void init(String[] args) throws CartagoException {
        CartagoContext cartagoCtx = CartagoService.startSession(CartagoService.MAIN_WSP_NAME, new AgentIdCredential("JaCaMo_Org_Launcher"));
        cartagoCtxs.put(CartagoService.MAIN_WSP_NAME, cartagoCtx);
    }
    
    @Override
    public void start() {
        for (JaCaMoOrgParameters o: project.getOrgs()) {
            try {
                // fix path for org
                o.addParameter("source", project.getOrgPaths().fixPath(o.getParameter("source")));

                // TODO: Just checking if the org wks exists is not a good solution, it must check if is already joined
                if (!CartagoService.getNode().getWorkspaces().contains(o.getName())) {
                    CartagoService.createWorkspace(o.getName());

                    logger.info("Workspace "+o.getName()+" created.");

                    CartagoContext cartagoCtx = null;
                    if (cartagoCtxs.containsKey(o.getName())) {
                        cartagoCtx = cartagoCtxs.get(o.getName());
                    } else {
                        cartagoCtx = CartagoService.startSession(o.getName(), new AgentIdCredential("JaCaMo_Org_Launcher"));
                        cartagoCtxs.put(o.getName(), cartagoCtx);
                    }
                
                    logger.info("getJoinedWorkspaces: " + cartagoCtx.getJoinedWorkspaces().toString());

                    WorkspaceId wid = cartagoCtx.joinWorkspace(o.getName(), new AgentIdCredential("JaCaMoLauncherAgOrg"));
                    
                    ArtifactId aid;
                    if (o.hasInstitution()) {
                        WorkspaceId instWid = cartagoCtx.joinWorkspace(o.getInstitution()); //, new AgentIdCredential("JaCaMoLauncherAgInst"));
                        ArtifactId instAId = cartagoCtx.lookupArtifact(instWid, o.getInstitution()+"_art");
                        aid = cartagoCtx.makeArtifact(wid, o.getName(), "sai.bridges.jacamo.OrgBoardSai", new Object[] { o.getParameter("source") } );
                        cartagoCtx.doAction(aid, new Op("setInstitution", new Object[] { o.getInstitution(), instAId } ));
                        logger.info("OrgBoard(SAI) "+o.getName()+" created.");
                    } else {
                        aid = cartagoCtx.makeArtifact(wid, o.getName(), "ora4mas.nopl.OrgBoard", new Object[] { o.getParameter("source") } );

                    }

                    // schemes
                    for (JaCaMoSchemeParameters s: o.getSchemes()) {
                        createScheme(aid, s, o, cartagoCtx);
                    }

                    // groups
                    for (JaCaMoGroupParameters g: o.getGroups()) {
                        createGroup(aid,null,g,o, cartagoCtx);
                    }
                    //EnvironmentWebInspector.get().registerWorkspace(o.getName());

                    //CartagoService.enableDebug(o.getName());
                } else {
                    logger.info("Workspace "+o.getName()+" already exists, organisation will not be created.");
                }
            } catch (CartagoException e) {
                e.printStackTrace();
            }
        }
    }

    protected void createGroup(ArtifactId orgB, JaCaMoGroupParameters parent, JaCaMoGroupParameters g, JaCaMoOrgParameters org, CartagoContext cartagoCtx) {
        String m = g.getName()+": "+g.getType()+" using artifact ora4mas.nopl.GroupBoard";

        try {
            OpFeedbackParam<ArtifactId> fb = new OpFeedbackParam<>();
            cartagoCtx.doAction(orgB, new Op("createGroup", new Object[] { g.getName(), g.getType(), fb} ));
            ArtifactId aid = fb.get();

            //artIds.put(g.getName(), aid);
            logger.info("group created: "+m);
            if (g.hasDebug())
                cartagoCtx.doAction(aid, new Op("debug", new Object[] { g.getDebugConf() } ));

            if (parent != null) {
                cartagoCtx.doAction(aid, new Op("setParentGroup", new Object[] { parent.getName() } ));
            }
            String owner = g.getParameter("owner");
            if (owner != null) {
                cartagoCtx.doAction(aid, new Op("setOwner", new Object[] { owner } ));
            }
            for (JaCaMoGroupParameters sg: g.getSubGroups()) {
                createGroup(orgB, g, sg, org, cartagoCtx);
            }

            //String respFor = g.getParameter("responsible-for"); // should be done after subgroup creation
            //if (respFor != null) {
            for (String respFor: g.getResponsibleFor()) {
                if (org.getScheme(respFor) == null)
                    logger.warning("** The scheme "+respFor+" does not existis in "+org.getName()+" so the group "+g.getName()+" cannot be responsible for it!");
                cartagoCtx.doAction(aid, new Op("addSchemeWhenFormationOk", new Object[] { respFor } ));
            }
        } catch (CartagoException e) {
            logger.log(Level.SEVERE, "error creating group "+m,e);
        }

    }

    protected void createScheme(ArtifactId orgB, JaCaMoSchemeParameters s, JaCaMoOrgParameters org, CartagoContext cartagoCtx) {
        String m = s.getName()+": "+s.getType()+" using artifact SchemeBoard";

        try {
            OpFeedbackParam<ArtifactId> fb = new OpFeedbackParam<>();
            cartagoCtx.doAction(orgB, new Op("createScheme", new Object[] { s.getName(), s.getType(), fb} ));
            ArtifactId aid = fb.get();

            //artIds.put(s.getName(), aid);
            logger.info("scheme created: "+m);
            if (s.hasDebug())
                cartagoCtx.doAction(aid, new Op("debug", new Object[] { s.getDebugConf() } ));

            String owner = s.getParameter("owner");
            if (owner != null) {
                cartagoCtx.doAction(aid, new Op("setOwner", new Object[] { owner } ));
            }
        } catch (CartagoException e) {
            logger.log(Level.SEVERE, "error creating scheme "+m,e);
        }
    }


}
