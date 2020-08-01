package jacamo.platform;

public class Sai extends DefaultPlatformImpl {
    
    // TODO: reimplement for cartago 3
/*
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

                saiArtId = cartagoCtx.makeArtifact(
                        wid, inst.getName()+"_art", 
                        "sai.bridges.jacamo.ConstitutiveArt", 
                        new Object[] { inst.getName(), inst.getParameter("source") } );
                
                // get listener object from SAI
                OpFeedbackParam<Object> fbre = new OpFeedbackParam<>();
                cartagoCtx.doAction(saiArtId, new Op("getRuleEngine", new Object[] { fbre } ));
                inst.setRE(fbre.get());
                                
                EnvironmentWebInspector.get().registerWorkspace(inst.getName());

            } catch (CartagoException e) {
                e.printStackTrace();
            }
        }
    }
    */
}
