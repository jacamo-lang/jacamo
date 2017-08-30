package jacamo.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * JaCaMo configuration
 *
 * @author jomi
 */
public class Config extends jason.util.Config {

    private static final long  serialVersionUID = 1L;

    public static final String jacamoHomeProp = "JaCaMoHome";
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


    public Config() {
        super();
    }

    @Override
    public InputStream getDetaultResource(String templateName) throws IOException {
        return Config.class.getResource("/templates/"+templateName).openStream();
    }

    @Override
    protected String getHome() {
        return getJaCaMoHome();
    }

    /** returns the file where the user preferences are stored */
    public File getUserConfFile() {
        return new File(System.getProperties().get("user.home") + File.separator + ".jacamo/user.properties");
    }

    public File getMasterConfFile() {
        return new File("jacamo.properties");
    }

    public String getFileConfComment() {
        return "JaCaMo user configuration";
    }

    @Override
    protected String getEclipseInstallationDirectory() {
        return "jacamo";
    }

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
        return "JaCaMo "+getJaCaMoVersion()+"\n"+
               "     built on "+getJasonBuiltDate()+"\n"+
               "     installed at "+getJaCaMoHome();
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

    @Override
    public String getJasonJar() {
        String jj = super.getJasonJar();
        if (jj == null) {
            // try to solve it by jacamo.jar
            try {
                File jasonjardir = new File(getJaCaMoHome()+"/libs").getAbsoluteFile().getCanonicalFile();
                String jarFile = findJarInDirectory(jasonjardir, "jason");
                if (checkJar(jarFile, 800000)) {
                    put(JASON_JAR, jarFile);
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
    public void fix() {
        tryToFixJarFileConf(JACAMO_JAR, "jacamo",   5000);
        tryToFixJarFileConf(MOISE_JAR,  "moise",   5000);
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

    public static void main(String[] args) {
        Config.get().fix();
        Config.get().store();
    }
}
