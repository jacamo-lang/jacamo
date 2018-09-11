package jacamo.platform;

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
import ora4mas.nopl.GroupBoard;
import ora4mas.nopl.OrgBoard;
import ora4mas.nopl.SchemeBoard;

public class Moise extends DefaultPlatformImpl {
    
    protected CartagoContext      cartagoCtx;

    Logger logger = Logger.getLogger(Moise.class.getName());

    @Override
    public void init(String[] args) throws CartagoException {
        cartagoCtx = CartagoService.startSession(CartagoService.MAIN_WSP_NAME, new AgentIdCredential("JaCaMo_Org_Launcher"));
    }
    
    @Override
    public void start() {
        for (JaCaMoOrgParameters o: project.getOrgs()) {
            try {
                if (project.isInDeployment(o.getNode())) {
                    if (project.getNodeHost(o.getNode()) != null) {
                        logger.warning("**** Remote organisation creation is not implemented yet! The organisation @ "+project.getNodeHost(o.getNode())+" wasn't created");
                        continue;
                    }
                    // fix path for org
                    o.addParameter("source", project.getOrgPaths().fixPath(o.getParameter("source")));

                    CartagoService.createWorkspace(o.getName());
                    logger.info("Workspace "+o.getName()+" created.");

                    cartagoCtx.joinWorkspace(o.getName(), new AgentIdCredential("JaCaMoLauncherAg"));
                    WorkspaceId wid = cartagoCtx.getJoinedWspId(o.getName());

                    ArtifactId aid = cartagoCtx.makeArtifact(wid, o.getName(), OrgBoard.class.getName(), new Object[] { o.getParameter("source") } );

                    // schemes
                    for (JaCaMoSchemeParameters s: o.getSchemes()) {
                        createScheme(aid, s, o);
                    }

                    // groups
                    for (JaCaMoGroupParameters g: o.getGroups()) {
                        createGroup(aid,null,g,o);
                    }
                    //CartagoService.enableDebug(o.getName());
                }
            } catch (CartagoException e) {
                e.printStackTrace();
            }
        }
    }

    protected void createGroup(ArtifactId orgB, JaCaMoGroupParameters parent, JaCaMoGroupParameters g, JaCaMoOrgParameters org) {
        String m = g.getName()+": "+g.getType()+" using artifact "+GroupBoard.class.getName();

        try {
            OpFeedbackParam<ArtifactId> fb = new OpFeedbackParam<ArtifactId>();
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
                createGroup(orgB, g, sg, org);
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

    protected void createScheme(ArtifactId orgB, JaCaMoSchemeParameters s, JaCaMoOrgParameters org) {
        String m = s.getName()+": "+s.getType()+" using artifact "+SchemeBoard.class.getName();

        try {
            OpFeedbackParam<ArtifactId> fb = new OpFeedbackParam<ArtifactId>();
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
