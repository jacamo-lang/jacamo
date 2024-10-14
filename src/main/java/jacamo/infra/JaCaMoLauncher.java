package jacamo.infra;

import jaca.CAgentArch;
import jacamo.platform.*;
import jacamo.project.JaCaMoProject;
import jacamo.project.parser.JaCaMoProjectParser;
import jacamo.project.parser.ParseException;
import jacamo.util.Config;
import jason.JasonException;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.directives.DirectiveProcessor;
import jason.asSyntax.directives.Include;
import jason.infra.local.RunLocalMAS;
import jason.mas2j.AgentParameters;
import jason.runtime.MASConsoleGUI;
import jason.runtime.MASConsoleLogHandler;
import jason.runtime.RuntimeServicesFactory;
import jason.runtime.SourcePath;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runs MASProject using JaCaMo infrastructure.
 *
 * This class reads the JCM project and creates the
 * corresponding environment, organisation, agents, ...
 *
 * @author Jomi
 */
public class JaCaMoLauncher extends RunLocalMAS {

    protected List<Platform> platforms = new ArrayList<>();

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public static String defaultProjectFileName = "default.jcm";

    public static void main(String[] args) throws JasonException {

        for (int i=0; i<args.length; i++) {
            String arg = args[i].trim();
            if ("-h".equals(arg)) {
                System.out.println("Usage jacamo <jcm-file> -v -h --debug --log-conf <log.properties file>");
                System.exit(0);
            }
            if ("-v".equals(arg)) {
                System.out.println(Config.get().getPresentation());
                System.exit(0);
            }
        }

        logger = Logger.getLogger(JaCaMoLauncher.class.getName());
        JaCaMoLauncher r = new JaCaMoLauncher();
        runner = r;
        RuntimeServicesFactory.set( new JaCaMoRuntimeServices(runner) );
        r.init(args);
        r.registerMBean();
        r.registerInRMI();
        r.registerWebMindInspector();
        r.create();
        r.start();
        r.waitEnd();
        r.finish(0, true, 0);
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

    @Override
    public int init(String[] args) {
        parseArgs(args);
        String projectFileName = null;
        if (RunLocalMAS.class.getResource("/"+defaultProjectFileName) != null) {
            projectFileName = defaultProjectFileName;
            appFromClassPath = true;
            //Config.get(false); // to void to call fix/store the configuration in this case everything is read from a jar/jnlp file
            Config.get().setShowFixMsgs(false);
            Config.get().fix();
            if (args.length == 1) {
                projectFileName = args[0];              
            }
        } else {
            if (args.length < 1) {
                System.out.println("JaCaMo "+Config.get().getJaCaMoVersion());
                System.err.println("You should inform the project file.");
                JOptionPane.showMessageDialog(null,"You should inform the project file as a parameter.\n\nJaCaMo version "+Config.get().getJaCaMoVersion(),"JaCaMo", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            } else {
                Config.get(); // to setup the JaCaMo Config
                projectFileName = args[0];
            }
        }

        // jacamo.jar and moise.jar must be configured (for includes)
        if (Config.get().getJaCaMoHome().isEmpty() || Config.get().get(Config.MOISE_JAR) == null) {
            //if (Config.get().getUserConfFile().exists())
            //    System.out.println("JaCaMo is not configured, creating a default configuration.");
            //else
            Config.get().setShowFixMsgs(false);
            Config.get().fix();             
        }

        setupLogger((String)initArgs.get("log-conf"));

        if ((boolean)(initArgs.getOrDefault("debug", false))) {
            debug = true;
            Logger.getLogger("").setLevel(Level.FINE);
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
            String urlPrefix = null;

            InputStream inProject;
            if (appFromClassPath) {
                if (projectFileName == null) {
                    inProject = RunLocalMAS.class.getResource("/"+defaultProjectFileName).openStream();
                } else {
                    inProject = RunLocalMAS.class.getResource("/"+projectFileName).openStream();                  
                }
                urlPrefix = SourcePath.CRPrefix;
            } else {
                URL file;
                // test if the argument is an URL
                try {
                    projectFileName = new SourcePath().fixPath(projectFileName); // replace $jason, if necessary
                    file = new URL(projectFileName);
                    if (projectFileName.startsWith("jar")) {
                        urlPrefix = projectFileName.substring(0,projectFileName.indexOf("!")+1);
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
            getJaCaMoProject().addSourcePath(urlPrefix);
            project.registerDirectives();
            // set the aslSrcPath in the include
            ((Include)DirectiveProcessor.getDirective("include")).setSourcePath(project.getSourcePaths());

            if (MASConsoleGUI.hasConsole()) {
                MASConsoleGUI.get().setTitle("MAS Console - " + project.getSocName());

                if (!project.isJade()) {
                    super.createButtons();
                }
            }

            if (initArgs.get("deploy-hosts") != null)
                getJaCaMoProject().setDeployHosts((String)initArgs.get("deploy-hosts"));

            loadPackages();

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
    protected void parseArgs(String[] args) {
        super.parseArgs(args);
        if (args.length > 0) {
            String la = "";
            for (String arg: args) {
                arg = arg.trim();
                if (la.equals("--deploy-hosts")) {
                    initArgs.put("deploy-hosts", arg);
                }
                la = arg;
            }
        }
    }

    void createCustomPlatforms() {
        boolean einsp = false;
        Platform p = null;
        for (String pId: getJaCaMoProject().getCustomPlatforms()) {
            try {               
                p = (Platform)Class.forName(pId).getConstructor().newInstance();
                p.setJcmProject(getJaCaMoProject());
                p.init( getJaCaMoProject().getPlatformParameters(pId) );
                platforms.add(p);
                einsp = einsp || pId.equals(EnvironmentWebInspector.class.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // include our own platforms
        if (!einsp) {
            p = new EnvironmentWebInspector();
            try {
                p.init(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            platforms.add(0,p);
        }
    }

    /** get packages from the project and add them into Config (map from pkg id -> file), used by .include */
    public void loadPackages() {
        var pkgs = getJaCaMoProject().getPackages();
        for (String k: pkgs.keySet()) {
            var ok = false;
            var f = new File(pkgs.get(k));
            if (f.exists()) {
                Config.get().addPackage(k, f);
                ok = true;
            } else {
                var args = pkgs.get(k).split(":");
                if (args.length == 3) {
                    // find package on classpath and fix Config (either gradle or ant should add the jar in the classpath before calling Launcher)
                    StringTokenizer st = new StringTokenizer(System.getProperty("java.class.path"), File.pathSeparator);
                    while (st.hasMoreTokens()) {
                        var jar = st.nextToken();
                        if (testJarFileForPkg(jar, args)) {
                            //System.out.println("solve package " + k + " with " + jar);
                            f = new File(jar);
                            if (f.exists()) {
                                Config.get().addPackage(k, f);
                                ok = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!ok) {
                logger.warning("the jar file for package '"+k+"' is not found (based on "+f+")");
            }
        }
    }

    private boolean testJarFileForPkg(String jarFileName, String[] args) {
        var pkgOrg = args[0];
        // in case of maven local, the "." in org name is replaced by "/"
        if (jarFileName.contains(".m2")) {
            pkgOrg = pkgOrg.replaceAll("[.]", "/");
        }
        //System.out.println("test2 "+jarFileName+" for "+pkgOrg+":"+args[1]+":"+args[2]);
        return jarFileName.endsWith(".jar")
                && jarFileName.contains(pkgOrg)
                && jarFileName.contains(args[1])
                && jarFileName.contains(args[2]);
    }

    @Override
    public void finish(int deadline, boolean stopJVM, int exitValue) {     
        stopAgs(deadline); // stop the agents before shutting down the platforms
        
        for (Platform p: platforms) {
            try {
                p.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.finish(deadline, stopJVM, exitValue); // call to stop JVM
    }
    
    private static String defaultLogProperties = "/templates/" + logPropFile;
    public static void setDefaultLogProperties(String lp) {
        defaultLogProperties = lp;
    }
    
    @Override
    protected InputStream getDefaultLogProperties() throws IOException {
        return JaCaMoLauncher.class.getResource(defaultLogProperties).openStream();
    }

    /** create environment, agents, controller */
    @Override
    public void create() throws JasonException {
        createCustomPlatforms();
        createEnvironment();
        createOrganisation();
        createInstitution();
        createAgs();
        //createController();        
    }

    @Override
    public void start() {
        for (Platform p: platforms) {
            try {
                p.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }        
        super.start(); // start agents after platforms
    }

    public void createEnvironment() throws JasonException {
        Cartago p = new Cartago();
        p.setJcmProject(getJaCaMoProject());
        try {
            p.init(getJaCaMoProject().getPlatformParameters("cartago") );
            platforms.add(Math.min(platforms.size(), 2), p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void createInstitution() {
        if (!getJaCaMoProject().getInstitutions().isEmpty()) {
            Sai p = new Sai();
            p.setJcmProject(getJaCaMoProject());
            try {
                p.init(getJaCaMoProject().getPlatformParameters("moise") );
                platforms.add(1,p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
        

    protected void createOrganisation() {
        if (!getJaCaMoProject().getOrgs().isEmpty()) {
            Moise p = new Moise();
            p.setJcmProject(getJaCaMoProject());
            try {
                p.init(getJaCaMoProject().getPlatformParameters("moise") );
                platforms.add(1,p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
        
    @Override
    public void createAgs() throws JasonException {
        // add jacamo archs
        List<AgentParameters> lags = new ArrayList<>();
        for (AgentParameters ap: getJaCaMoProject().getAgents()) {
            if (ap.getNbInstances() > 0) {
                /*if (getJaCaMoProject().getNodeHost(ap.getHost()) != null) {
                    logger.warning("**** Remote agent creation is not implemented yet! The agent "+ap.getAgName()+" @ "+getJaCaMoProject().getNodeHost(ap.getHost())+" wasn't created");
                    continue;
                }*/
                lags.add(ap);
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
        project.fixAgentsSrc();

        Jade jadePlat = null;
        if (getProject().isJade()) {
            jadePlat = new Jade();
            jadePlat.setJcmProject(getJaCaMoProject());
            try {
                jadePlat.init(getJaCaMoProject().getPlatformParameters("jade"));
                platforms.add(jadePlat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.createAgs();
        }
    }

    @Override
    protected void startAgs() {
        if (!getProject().isJade()) {
            super.startAgs();
        }
    }

//    @Override
//    protected void createReplAg(String n) {
//        LocalAgArch agArch = new LocalAgArch();
//        try {
//            agArch.setAgName(n);
//            List<String> archs = new ArrayList<>();
//            archs.add(CAgentArch.class.getName());
//            archs.add(JaCaMoAgArch.class.getName());
//
//            agArch.createArchs(archs, ReplAgGUI.class.getName(), null, null, new Settings());
//            Thread agThread = new Thread(agArch);
//            agArch.setThread(agThread);
//            agThread.start();
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        addAg(agArch);
//    }
//
}
