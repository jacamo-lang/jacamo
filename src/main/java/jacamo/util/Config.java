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

    private static Config      singleton     = null;

    static {
        jason.util.Config.setClassFactory(Config.class.getName());        
    }
    
    public static Config get() {
        return get(true);
    }

    public static Config get(boolean tryToFixConfig) {
        if (singleton == null) {
            singleton = new Config();
            if (!singleton.load()) {
                if (tryToFixConfig) {
                    singleton.fix();
                    singleton.store();
                }
            }
        }
        return singleton;
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


    /** Set most important parameters with default values */
    public void fix() {
        tryToFixJarFileConf(JACAMO_JAR, "jacamo",   5000);
        super.fix();
        
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
