package jacamo.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import jacamo.infra.RunJaCaMoProject;
import ora4mas.nopl.GroupBoard;

/**
 * JaCaMo configuration
 *
 * @author jomi
 */
public class Config extends jason.util.Config {

    private static final long  serialVersionUID = 1L;

    public static final String jacamoHomeProp = "JaCaMoHome";

    @Deprecated
    public static final String DOT_PATH       = "dotPath";

    public static final String JACAMO_JAR    = "jacamoJar";
    public static final String MOISE_JAR     = "moiseJar";

    static {
        jason.util.Config.setClassFactory(Config.class.getName());
    }

    public static Config get() {
        return get(false);
    }

    public static Config get(boolean tryToFixConfig) {
        if (singleton == null || !singleton.getClass().getName().equals(Config.class.getName())) {
            jason.util.Config.setClassFactory(Config.class.getName());
            singleton = new Config();
            if (!singleton.load()) {
                if (tryToFixConfig) {
                    singleton.fix();
                    singleton.store();
                }
            }
        }
        return (Config)singleton;
    }

    @Override
    public boolean load() {
        boolean r = super.load();
        if (r) {
            String jarFile = getJarFromClassPath("jacamo", getJarFileForFixTest(JACAMO_JAR));
            if (checkJar(jarFile, getJarFileForFixTest(JACAMO_JAR))) {
                if (getJaCaMoJar() != null && !getJaCaMoJar().equals(jarFile)) {
                    System.out.println("\n\n*** The jacamo.jar from classpath is different than jacamo.jar from configuration, consider to delete the configuration (file ~/.jacamo/user.properties or jacamo.properties).");
                    System.out.println("Classpath is\n   "+jarFile+
                                     "\nConfig    is\n   "+getJaCaMoJar()+"\n\n");
                }                
                put(JACAMO_JAR, jarFile); // always prefer classpath jar
            }
        }
        return r;
    }

    public Config() {
        super();
    }

    @Override
    public InputStream getDetaultResource(String templateName) throws IOException {
        return new URL("jar:file:"+getJaCaMoJar()+"!/templates/"+templateName).openStream();
    }

    @Override
    protected String getHome() {
        return getJaCaMoHome();
    }

    /** returns the file where the user preferences are stored */
    public File getUserConfFile() {
        return new File(System.getProperties().get("user.home") + File.separator + ".jacamo/user.properties");
    }

    @Override
    public File getLocalConfFile() {
        return new File("jacamo.properties");
    }

    public String getFileConfComment() {
        return "JaCaMo user configuration";
    }

    @Override
    protected String getEclipseInstallationDirectory() {
        return "jacamo";
    }

    @Deprecated
    public String getDotPath() {
        String r = super.getProperty(DOT_PATH);
        if (r == null)
            r = "/opt/local/bin/dot";
        if (new File(r).exists())
            return r;
        else {
            r = "/usr/bin/dot";
            if (new File(r).exists())
                return r;
        }
        return null;
    }

    public String getPresentation() {
        return "JaCaMo "+getJaCaMoVersion()+" built on "+getJaCaMoBuiltDate()+"\n"; 
               //"     installed at "+getJaCaMoHome();
    }

    /**
     * @return the jacamo home (based on jacamo.jar)
     */
    public String getJaCaMoHome() {
        try {
            if (get(JACAMO_JAR) != null)
                return new File(get(JACAMO_JAR).toString()).getParentFile().getParent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getJaCaMoJar() {
        return getProperty(JACAMO_JAR);     
    }
    
    @Override
    public String getJasonJar() {
        String jj = super.getJasonJar();
        if (jj == null) {
            // try to solve it by jacamo.jar
            try {
                File jasonjardir = new File(getJaCaMoHome()+"/libs").getAbsoluteFile().getCanonicalFile();
                String jarFile = findJarInDirectory(jasonjardir, "jason");
                if (checkJar(jarFile, getJarFileForFixTest(JASON_JAR))) {
                    put(JASON_JAR, jarFile);
                    if (showFixMsgs)
                        System.out.println("found at " + jarFile+" by JaCaMo HOME");
                    jj = jarFile;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jj;
    }

    /** Set most important parameters with default values */
    @Override
    public void fix() {
        tryToFixJarFileConf(JACAMO_JAR, "jacamo"); // this jar is required at runtime (e.g. for .include)
        tryToFixJarFileConf(MOISE_JAR,  "moise"); // this jar is required at runtime (e.g. for .include)
        super.fix();
        
        if (getProperty(START_WEB_EI) == null) {
            put(START_WEB_EI, "true");
        }
        if (getProperty(START_WEB_OI) == null) {
            put(START_WEB_OI, "true");
        }

        if (get(ANT_LIB) == null || !checkAntLib(getAntLib())) {
            try {
                String jjar = getJaCaMoHome();
                if (jjar != null) {
                    String antlib = jjar + File.separator + "libs";
                    if (checkAntLib(antlib)) {
                        setAntLib(antlib);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error setting ant lib!");
                e.printStackTrace();
            }
        }
    }
    
    @Override
    @SuppressWarnings("rawtypes")
    public Class getClassForClassLoaderTest(String jarEntry) {
        if (jarEntry == JACAMO_JAR)
            try {
                return RunJaCaMoProject.class;
            } catch (Throwable e) {}; // class not found
        if (jarEntry == MOISE_JAR)
            try {
                return GroupBoard.class;
            } catch (Throwable e) {}; // class not found
        return super.getClassForClassLoaderTest(jarEntry);
    }
  

    @Override
    public String getJarFileForFixTest(String jarEntry) {
        if (jarEntry == JACAMO_JAR)
            try {
                return "jacamo/infra/RunJaCaMoProject.class";
            } catch (Throwable e) {}; // class not found
        if (jarEntry == MOISE_JAR)
            try {
                return "ora4mas/nopl/GroupBoard.class";
            } catch (Throwable e) {}; // class not found
        return super.getJarFileForFixTest(jarEntry);
    }
    
    @Override
    public boolean tryToFixJarFileConf(String jarEntry, String jarFilePrefix) {
        super.tryToFixJarFileConf(jarEntry, jarFilePrefix);
        // for moise.jar we need to fix based on jacamohome, since when running Config or ConfigGUI it is not in the classpath
        // latter, eclipse requires these jars
        if (get(jarEntry) == null && getJaCaMoHome() != null) { // super didn't solve
            String jarFile = findJarInDirectory(new File(getJaCaMoHome()+"/libs"), jarFilePrefix);
            if (checkJar(jarFile, getJarFileForFixTest(jarEntry))) {
                try {
                    put(jarEntry, new File(jarFile).getCanonicalFile().getAbsolutePath());
                    if (showFixMsgs)
                        System.out.println("found at " + jarFile + " based on location of jacamo.jar");
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        // try with $JACAMO_HOME
        String jh = System.getenv().get("JACAMO_HOME");
        if (jh != null) {
            String jarFile = findJarInDirectory(new File(jh+"/libs"), jarFilePrefix);
            if (checkJar(jarFile, getJarFileForFixTest(jarEntry))) {
                try {
                    put(jarEntry, new File(jarFile).getCanonicalFile().getAbsolutePath());
                    if (showFixMsgs)
                        System.out.println("found at " + jarFile + " based on JACAMO_HOME");
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }               
        }
        return false;
    }

    @Override
    public String getAntLib() {
        String al = super.getAntLib();
        if (al == null) {
            return getJaCaMoHome()+"/libs";
        }
        return al;
    }

    public String getJaCaMoVersion() {
        Package j = Package.getPackage("jacamo.util");
        if (j != null) {
            return j.getSpecificationVersion();
        }
        return "?";
    }
    
    public String getJaCaMoBuiltDate() {
        Package j = Package.getPackage("jacamo.util");
        if (j != null) {
            return j.getImplementationVersion();
        }
        return "?";
    }

    public static void main(String[] args) {
        Config.get().fix();
        Config.get().store();
    }
}
