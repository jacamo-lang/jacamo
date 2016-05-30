package jacamo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * class used to create an initial jacamo project: 
 * 
 *  directory structure and
 *  example of programs
 *  
 * @author jomi
 */
public class CreateNewProject {
    
    File main, path;
    String id;
    Config c = Config.get();
    
    public CreateNewProject(File m) {
        main = m;
        path = main.getAbsoluteFile(); //.getParentFile();
        
        id = main.getName();
        id = id.substring(0,1).toLowerCase() + id.substring(1);
    }


    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("usage must be:");
            System.out.println("      java "+CreateNewProject.class.getName()+" <id of new application>");
            return;
        } 
        
        CreateNewProject p = new CreateNewProject(new File(args[0]));        
        p.createDirs();
        p.copyFiles();
    }

    void createDirs() {
        if (!path.exists()) {
            System.out.println("Creating path "+path);
            path.mkdirs();
        }
        
        new File(path + "/doc").mkdirs();
        new File(path + "/lib").mkdirs();
        new File(path + "/log").mkdirs();
        new File(path + "/src/agt/inc").mkdirs();
        new File(path + "/src/agt/jia").mkdirs();
        new File(path + "/src/env/tools").mkdirs();
        new File(path + "/src/org").mkdirs();
        new File(path + "/src/int").mkdirs();
    }
    
    void copyFiles() {
        copyFile("project", new File( path+"/"+main+".jcm"));
        copyFile("agent", new File( path + "/src/agt/sample_agent.asl"));
        copyFile("logging.properties", new File( path + "/logging.properties"));
        copyFile("CArtAgOartifact", new File(path+"/src/env/tools/Counter.java"));
        copyFile("organization", new File( path + "/src/org/org.xml"));
        copyFile("build.gradle", new File( path + "/build.gradle"));
    }

    void copyFile(String source, File target) {
        try {
            BufferedReader in = new BufferedReader( new InputStreamReader( c.getDetaultResource(source) ));
            BufferedWriter out = new BufferedWriter(new FileWriter(target));
            String l = in.readLine();
            while (l != null) {
                l = l.replace("<PROJECT_NAME>", id);
                l = l.replace("<PLATFORM>", "");
                l = l.replace("<PROJECT-FILE>", main+".jcm");
                l = l.replace("<VERSION>", c.getJaCaMoVersion());
                l = l.replace("<DATE>", new SimpleDateFormat("MMMM dd, yyyy - HH:mm:ss").format(new Date()));

                l = l.replace("<DEFAULT_AGENT>", "agent sample_agent");
                l = l.replace("<AG_NAME>", "sample_agent");
                
                l = l.replace("<PCK>", "tool");
                l = l.replace("<ARTIFACT_NAME>", "Counter");
                l = l.replace("<SUPER_CLASS>", "Artifact");
                
                l = l.replace("<ORGANIZATION_NAME>", id);
                out.append(l+"\n");
                l = in.readLine();
            }
            out.close();        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
