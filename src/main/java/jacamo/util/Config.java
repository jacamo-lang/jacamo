package jacamo.util;

import jacamo.infra.RunJaCaMoProject;
import ora4mas.nopl.GroupBoard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.net.URL;
import java.util.logging.Logger;

/**
 * JaCaMo configuration
 *
 * @author jomi
 */
public class Config extends jason.util.Config {

    @Serial
    private static final long  serialVersionUID = 1L;

    public static final String jacamoHomeProp = "JaCaMoHome";

    public static final String JACAMO_JAR    = "jacamoJar";
    public static final String JACAMO_PKG    = "jacamo";
    public static final String MOISE_JAR     = "moiseJar";
    public static final String MOISE_PKG     = "moise";

    static {
        jason.util.Config.setClassFactory(Config.class.getName());
    }

    Logger logger = Logger.getLogger(Config .class.getName());

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
                    //singleton.store();
                }
            }
        }
        return (Config)singleton;
    }

    @Override
    public void store() {
        // do not store config anymore
    }

    @Override
    public boolean load() {
        boolean r = super.load();
        if (r) {
            String jarFile = getJarFromClassPath("jacamo", getJarFileForFixTest(JACAMO_JAR));
            if (checkJar(jarFile, getJarFileForFixTest(JACAMO_JAR))) {
                if (getJaCaMoJar() != null && !getJaCaMoJar().equals(jarFile)) {
                    System.out.println("\n\n*** The jacamo.jar from classpath is different than jacamo.jar from configuration, consider to delete the configuration (file ~/.jacamo/user.properties or jacamo.properties or unset JACAMO_HOME).");
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
    public InputStream getDefaultResource(String templateName) throws IOException {
        return new URL("jar:file:"+getJaCaMoJar()+"!/templates/"+templateName).openStream();
    }

    public synchronized Object put(Object key, Object value) {
        if (JACAMO_JAR.equals(key)) {
            addPackage(JACAMO_PKG, new File((String)value));
            addPackage(JACAMO_JAR, new File((String)value)); // for compatibility reasons
        }
        if (MOISE_JAR.equals(key)) {
            addPackage(MOISE_PKG, new File((String)value));
            addPackage(MOISE_JAR, new File((String)value));  // for compatibility reasons
        }
        return super.put(key, value);
    }

    @Override
    protected String getHome() {
        return getJaCaMoHome();
    }

    /** returns the file where the user preferences are stored */
    public File getUserConfFile() {
        return new File(System.getProperties().get("user.home") + File.separator + ".jacamo/user.properties"+"xxxxxx"); // so to not find the file and not use this file anymore
    }

    @Override
    public File getLocalConfFile() {
        return new File("jacamo.properties");
    }

    public String getFileConfComment() {
        return "JaCaMo user configuration";
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
        tryToFixJarFileConf(MOISE_JAR,  "moise");  // this jar is required at runtime (e.g. for .include)
        super.fix();

        if (getProperty(START_WEB_EI) == null) {
            put(START_WEB_EI, "true");
        }
        if (getProperty(START_WEB_OI) == null) {
            put(START_WEB_OI, "true");
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class getClassForClassLoaderTest(String jarEntry) {
        if (jarEntry == JACAMO_JAR)
            try {
                return RunJaCaMoProject.class;
            } catch (Throwable e) {} // class not found
        if (jarEntry == MOISE_JAR)
            try {
                return GroupBoard.class;
            } catch (Throwable e) {} // class not found
        return super.getClassForClassLoaderTest(jarEntry);
    }


    @Override
    public String getJarFileForFixTest(String jarEntry) {
        if (jarEntry == JACAMO_JAR)
            try {
                return "jacamo/infra/RunJaCaMoProject.class";
            } catch (Throwable e) {} // class not found
        if (jarEntry == MOISE_JAR)
            try {
                return "ora4mas/nopl/GroupBoard.class";
            } catch (Throwable e) {} // class not found
        return super.getJarFileForFixTest(jarEntry);
    }

    public String findJarInDirectory(File dir, String prefix, String jarEntry) {
        if (dir.isDirectory()) {
            for (File f: dir.listFiles()) {
                if (f.getName().startsWith(prefix) &&
                        f.getName().endsWith(".jar") &&
                        !f.getName().endsWith("-sources.jar") &&
                        !f.getName().endsWith("-javadoc.jar") &&
                        checkJar(f.getAbsolutePath(), getJarFileForFixTest(jarEntry))) {
                    return f.getAbsolutePath();
                }
            }
        }
        return null;
    }

    @Override
    public boolean tryToFixJarFileConf(String jarEntry, String jarFilePrefix) {
        String jarFile   = getProperty(jarEntry);
        String fileInJar = getJarFileForFixTest(jarEntry);

        if (jarFile == null || !checkJar(jarFile, fileInJar)) {

            // try to get by class loader
            try {
                String fromLoader = getClassForClassLoaderTest(jarEntry).getProtectionDomain().getCodeSource().getLocation().toString();
                if (fromLoader.startsWith("file:"))
                    fromLoader = fromLoader.substring(5);
                if (new File(fromLoader).getName().startsWith(jarFilePrefix) && checkJar(fromLoader, fileInJar)) {
                    var msg = "Configuration of '"+jarEntry+"' found at " + fromLoader+", based on class loader";
                    logger.finer(msg);
                    if (showFixMsgs)
                        System.out.println(msg);
                    put(jarEntry, fromLoader);
                    return true;
                }
            } catch (Exception e) {}
            if (showFixMsgs)
                System.out.println("Configuration of '"+jarEntry+"' NOT found, based on class loader");

            // try to get from classpath
            jarFile = getJarFromClassPath(jarFilePrefix, fileInJar);
            if (checkJar(jarFile, fileInJar)) {
                var msg = "Configuration of '"+jarEntry+"' found at " + jarFile+", based on classpath";
                logger.finer(msg);
                if (showFixMsgs)
                    System.out.println(msg);
                put(jarEntry, jarFile);
                return true;
            }
            if (showFixMsgs)
                System.out.println("Configuration of '"+jarEntry+"' NOT found, based on class path: "+System.getProperty("java.class.path"));

            // try with $JACAMO_HOME
            String jh = System.getenv().get("JACAMO_HOME");
            if (jh != null) {
                jarFile = findJarInDirectory(new File(jh+"/libs"), jarFilePrefix, jarEntry);
                if (jarFile != null) {
                    try {
                        var msg = "Configuration of '"+jarEntry+"' found at " + jarFile + ", based on JACAMO_HOME variable: "+jh;
                        logger.finer(msg);
                        if (showFixMsgs)
                            System.out.println(msg);
                        put(jarEntry, new File(jarFile).getCanonicalFile().getAbsolutePath());
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (showFixMsgs)
                System.out.println("Configuration of '"+jarEntry+"' NOT found, based on JACAMO_HOME="+jh);

            super.tryToFixJarFileConf(jarEntry, jarFilePrefix);
        }


        // for moise.jar we need to fix based on jacamohome, since when running Config or ConfigGUI it is not in the classpath
        if (get(jarEntry) == null && getJaCaMoHome() != null) { // super didn't solve
            jarFile = findJarInDirectory(new File(getJaCaMoHome()+"/libs"), jarFilePrefix, jarEntry);
            if (jarFile != null) {
                try {
                    put(jarEntry, new File(jarFile).getCanonicalFile().getAbsolutePath());
                    if (showFixMsgs)
                        System.out.println("Configuration of '"+jarEntry+"' found at " + jarFile + " based on location of jacamo.jar");
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
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
        showFixMsgs = true;
        Config.get(true);
        //Config.get().store();
    }
}
