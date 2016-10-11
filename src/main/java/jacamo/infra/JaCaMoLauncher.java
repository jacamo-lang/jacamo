package jacamo.infra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import cartago.AgentIdCredential;
import cartago.ArtifactId;
import cartago.CartagoContext;
import cartago.CartagoException;
import cartago.CartagoService;
import cartago.Op;
import cartago.OpFeedbackParam;
import cartago.WorkspaceId;
import jaca.CAgentArch;
import jaca.CartagoEnvironment;
import jacamo.project.JaCaMoGroupParameters;
import jacamo.project.JaCaMoOrgParameters;
import jacamo.project.JaCaMoProject;
import jacamo.project.JaCaMoSchemeParameters;
import jacamo.project.JaCaMoWorkspaceParameters;
import jacamo.project.parser.JaCaMoProjectParser;
import jacamo.project.parser.ParseException;
import jacamo.util.Config;
import jason.JasonException;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.directives.DirectiveProcessor;
import jason.asSyntax.directives.Include;
import jason.infra.centralised.CentralisedAgArch;
import jason.infra.centralised.RunCentralisedMAS;
import jason.infra.jade.RunJadeMAS;
import jason.infra.repl.ReplAgGUI;
import jason.mas2j.AgentParameters;
import jason.mas2j.ClassParameters;
import jason.runtime.MASConsoleGUI;
import jason.runtime.MASConsoleLogHandler;
import jason.runtime.Settings;
import ora4mas.nopl.GroupBoard;
import ora4mas.nopl.OrgBoard;
import ora4mas.nopl.SchemeBoard;

/**
 * Runs MASProject using JaCaMo infrastructure.
 * 
 * This class reads the mas2j project and create the 
 * corresponding environment, organisation, agents, ...
 * 
 * @author Jomi
 */
public class JaCaMoLauncher extends RunCentralisedMAS {
    
    protected CartagoEnvironment  env;
    protected CartagoContext      cartagoCtx;
    
    protected Map<String, ArtifactId> artIds = new HashMap<String, ArtifactId>();
    
    protected RunJadeMAS rJADE = null;
    
    public static void main(String[] args) throws JasonException {
        logger = Logger.getLogger(JaCaMoLauncher.class.getName());
        JaCaMoLauncher r = new JaCaMoLauncher();
        runner = r;
        r.init(args);
        r.create();
        r.start();
        r.waitEnd();
        r.finish();
    }

    public static JaCaMoLauncher getJaCaMoRunner() {
        return (JaCaMoLauncher)runner;
    }
    public JaCaMoProject getJaCaMoProject() {
        if ( !(getProject() instanceof JaCaMoProject)) {
            // create a jacamo project based on mas2j project
            project = new JaCaMoProject(getProject());
        }
        return (JaCaMoProject)getProject();
    }
    
    public ArtifactId getArtId(String artName) {
        return artIds.get(artName);
    }
    
    @Override
    public int init(String[] args) {
        String projectFileName = null;
        if (args.length < 1) {
            if (RunCentralisedMAS.class.getResource("/"+defaultProjectFileName) != null) {
                projectFileName = defaultProjectFileName;
                readFromJAR = true;
                Config.get(false); // to void to call fix/store the configuration in this case everything is read from a jar/jnlp file
            } else {
                System.out.println("JaCaMo "+Config.get().getJaCaMoVersion());
                System.err.println("You should inform the project file.");
                JOptionPane.showMessageDialog(null,"You should inform the project file as a parameter.\n\nJaCaMo version "+Config.get().getJaCaMoVersion(),"JaCaMo", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        } else {
            Config.get(); // to setup the JaCaMo Config
            projectFileName = args[0];
        }

        setupLogger();

        if (args.length >= 2) {
            if (args[1].equals("-debug")) {
                debug = true;
                Logger.getLogger("").setLevel(Level.FINE);
            }
        }

        // discover the handler        
        for (Handler h : Logger.getLogger("").getHandlers()) {
            // if there is a MASConsoleLogHandler, show it
            if (h.getClass().toString().equals(MASConsoleLogHandler.class.toString())) {
                MASConsoleGUI.get().getFrame().setVisible(true);
                MASConsoleGUI.get().setAsDefaultOut();
            }
        }

        int errorCode = 0;

        try {
            InputStream inProject;
            if (readFromJAR) {
                inProject = RunCentralisedMAS.class.getResource("/"+defaultProjectFileName).openStream();
                urlPrefix = Include.CRPrefix + "/";
            } else {
                URL file;
                // test if the argument is an URL
                try {
                    file = new URL(projectFileName);
                    if (projectFileName.startsWith("jar")) {
                        urlPrefix = projectFileName.substring(0,projectFileName.indexOf("!")+1) + "/";
                    }
                } catch (Exception e) {
                    file = new URL("file:"+projectFileName);
                }
                inProject = file.openStream();
            }
            JaCaMoProjectParser parser = new JaCaMoProjectParser(inProject);
            
            String directory = null;
            try {
                directory = new File(projectFileName).getAbsoluteFile().getParentFile().toString();
            } catch (Exception e) {}
            if (directory == null) {
                directory = new File(".").getAbsoluteFile().getParentFile().toString();
            }
            try {
                project = parser.parse(directory);
            } catch (ParseException e) {
                if (e.toString().startsWith("jacamo.project.parser.ParseException: Encountered \"MAS\"")) {
                    // it is the case of a .mas2j file
                    return super.init(args);
                } else {
                    logger.log(Level.SEVERE, "Error parsing file " + projectFileName + "!", e);
                    errorCode = 3;
                    return errorCode;
                }
            }
            project.setupDefault();
            
            project.registerDirectives();
            // set the aslSrcPath in the include
            ((Include)DirectiveProcessor.getDirective("include")).setSourcePath(project.getSourcePaths());
            
            if (MASConsoleGUI.hasConsole()) {
                MASConsoleGUI.get().setTitle("MAS Console - " + project.getSocName());

                if (!project.isJade()) {
                    super.createButtons();
                }
            }

            //runner.waitEnd();
            errorCode = 0;

        } catch (FileNotFoundException e1) {
            logger.log(Level.SEVERE, "File " + projectFileName + " not found!");
            errorCode = 2;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error!?: ", e);
            errorCode = 4;
        }
        
        System.out.flush();
        System.err.flush();

        if (!MASConsoleGUI.hasConsole() && errorCode != 0) {
            System.exit(errorCode);
        }
        return errorCode;
    }
    
    @Override
    protected InputStream getDefaultLogProperties() throws IOException {
        return JaCaMoLauncher.class.getResource("/templates/" + logPropFile).openStream();
    }

    /** create environment, agents, controller */
    @Override
    public void create() throws JasonException {
        createEnvironment();
        createOrganisation();
        createAgs();
        //createController();        
    }

    @Override
    public void createEnvironment() throws JasonException {
        env = new CartagoEnvironment(); 
        String[] args = getJaCaMoProject().getPlatformParameters("cartago");
        if (args == null)
            args = new String[] {};
        env.init(args);
        if (! "false".equals(Config.get().getProperty(jason.util.Config.START_WEB_EI))) {
            EnvironmentInspectorWeb.startHttpServer();
            EnvironmentInspectorWeb.registerWorkspace(CartagoService.MAIN_WSP_NAME);
        }

        try {
            cartagoCtx = CartagoService.startSession(CartagoService.MAIN_WSP_NAME, new AgentIdCredential("JaCaMo_Launcher"));
        } catch (CartagoException e1) {
            e1.printStackTrace();
            return;
        }
        for (JaCaMoWorkspaceParameters wp: getJaCaMoProject().getWorkspaces()) {
            try {
                if (getJaCaMoProject().isInDeployment(wp.getNode())) {
                    if (getJaCaMoProject().getNodeHost(wp.getNode()) != null) {
                        logger.warning("**** Remote workspace creation is not implemented yet! The workspace @ "+getJaCaMoProject().getNodeHost(wp.getNode())+" wasn't created");
                        continue;
                    }
                    CartagoService.createWorkspace(wp.getName());
                    logger.info("Workspace "+wp.getName()+" created.");
                    EnvironmentInspectorWeb.registerWorkspace(wp.getName());

                    cartagoCtx.joinWorkspace(wp.getName(), new AgentIdCredential("JaCaMoLauncherAg"));
                    WorkspaceId wid = cartagoCtx.getJoinedWspId(wp.getName());
                    
                    for (String aName: wp.getArtifacts().keySet()) {
                        String m = null;
                        try {
                            ClassParameters cp = wp.getArtifacts().get(aName);
                            m = "artifact "+aName+": "+cp.getClassName()+"("+cp.getParametersStr(",")+") at "+wp.getName();
                            ArtifactId aid = cartagoCtx.makeArtifact(wid, aName, cp.getClassName(), cp.getTypedParametersArray());
                            artIds.put(aName, aid);
                            logger.info(m+" created.");
                            if (wp.hasDebug())
                                EnvironmentInspector.addInGui(wp.getName(), aid);
                        } catch (CartagoException e) {
                            logger.log(Level.SEVERE, "error creating "+m,e);
                        }
                    }    
                    if (wp.hasDebug()) {
                        CartagoService.enableDebug(wp.getName());
                    }
                }
            } catch (CartagoException e) {
                logger.log(Level.SEVERE, "error creating environmet, workspace:"+wp.getName(),e);
            }
        }
    }

    protected void createOrganisation() {
        for (JaCaMoOrgParameters o: getJaCaMoProject().getOrgs()) {
            try {
                if (getJaCaMoProject().isInDeployment(o.getNode())) {
                    if (getJaCaMoProject().getNodeHost(o.getNode()) != null) {
                        logger.warning("**** Remote organisation creation is not implemented yet! The organisation @ "+getJaCaMoProject().getNodeHost(o.getNode())+" wasn't created");
                        continue;
                    }
                    File spec = new File(o.getParameter("source"));
                    if (!spec.exists()) {
                        o.addParameter("source", "src/org/"+spec);
                    }
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
            
            artIds.put(g.getName(), aid);
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
            
            String respFor = g.getParameter("responsible-for"); // should be done after subgroup creation
            if (respFor != null) {
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

            artIds.put(s.getName(), aid);
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

    @Override
    public void createAgs() throws JasonException {
        if (getProject().isJade()) {
            rJADE = new RunJadeMAS();
            rJADE.setProject(project);
            rJADE.addInitArgs( getJaCaMoProject().getPlatformParameters("jade"));
            rJADE.createButtons();
            if (rJADE.startContainer()) {
                logger.info("Jade Container started.");
            } else {
                logger.log(Level.WARNING,"Error starting JADE container!");
                return;
            }
        }
        
        // add jacamo archs
        List<AgentParameters> lags = new ArrayList<AgentParameters>();
        for (AgentParameters ap: getJaCaMoProject().getAgents()) {
            if (ap.getNbInstances() > 0 && getJaCaMoProject().isInDeployment(ap.getHost())) {
                if (getJaCaMoProject().getNodeHost(ap.getHost()) != null) {
                    logger.warning("**** Remote agent creation is not implemented yet! The agent "+ap.getAgName()+" @ "+getJaCaMoProject().getNodeHost(ap.getHost())+" wasn't created");
                    continue;
                }
                lags.add(ap);
                ap.addArchClass(new ClassParameters(CAgentArch.class.getName()));
                ap.addArchClass(new ClassParameters(JaCaMoAgArch.class.getName()));
                
                // includes mind inspector
                String debug = ap.getOption("debug");
                if (debug != null) {
                    try {
                        Literal lDebug = ASSyntax.parseLiteral(debug);
                        ap.addOption(lDebug.getFunctor(), lDebug.getTerm(0).toString());
                    } catch (jason.asSyntax.parser.ParseException e) {
                        logger.log(Level.WARNING, "Error installing mind inspector for agent "+ap.getAgName(),e);
                    }
                }
            }
        }            
        project.getAgents().clear(); // clear only after iterating the getAgInstances!
        project.getAgents().addAll(lags);
        project.fixAgentsSrc(urlPrefix);
        
        if (getProject().isJade()) {
            rJADE.createAgs();
        } else {
            super.createAgs();
        }
    }    
    
    @Override
    protected void startAgs() {
        if (getProject().isJade()) {
            rJADE.startAgs();
        } else {
            super.startAgs();
        }
    }
    
    @Override
    protected void createReplAg(String n) {
        CentralisedAgArch agArch = new CentralisedAgArch();
        try {
            agArch.setAgName(n);
            List<String> archs = new ArrayList<String>();
            archs.add(CAgentArch.class.getName());
            archs.add(JaCaMoAgArch.class.getName());

            agArch.createArchs(archs, ReplAgGUI.class.getName(), null, null, new Settings(), JaCaMoLauncher.this);
            Thread agThread = new Thread(agArch);
            agArch.setThread(agThread);
            agThread.start();                            
        } catch (JasonException e1) {
            e1.printStackTrace();
        }
        addAg(agArch);
    }

}
