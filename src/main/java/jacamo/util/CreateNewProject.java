package jacamo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

/**
 * class used to create an initial jacamo application:
 *
 *  directory structure and
 *  example of programs
 *
 * @author jomi
 */
public class CreateNewProject {

    File main, path;
    String id;
    static Config c = Config.get();
    boolean consoleApp = false;

    public CreateNewProject(File m) {
        main = m;
        path = main.getAbsoluteFile(); //.getParentFile();

        id = main.getName();
        id = id.substring(0,1).toLowerCase() + id.substring(1);
        id = id.replace("-","_");
    }


    public static void main(String[] args) throws Exception {
        boolean consoleApp = false;
        for (int i=0; i<args.length; i++)
            if ("--console".equals(args[i])) 
                consoleApp = true;
        
        String pId = null;
        if (args.length == 0 || (args.length == 1 && consoleApp)) {
            System.out.println(Config.get().getPresentation()+"\n");
            /*System.out.println("usage must be:");
            System.out.println("      java "+CreateNewProject.class.getName()+" <id of new application>");
            return;*/
            System.out.print("\n\nEnter the identification of the new application: ");
            pId = new Scanner(System.in).nextLine();
            if (pId.length() == 0) {
                System.out.println("      you should enter a project id!");
                return;
            }
        } else {
            pId = args[0];
        }

        if (Config.get().getJaCaMoHome().isEmpty()) {
            //if (Config.get().getUserConfFile().exists())
            //    System.out.println("JaCaMo is not configured, creating a default configuration.");
            //else
            Config.get().setShowFixMsgs(false);
            Config.get().fix();
        }

        CreateNewProject p = new CreateNewProject(new File(pId));
        p.consoleApp = consoleApp;
        p.createDirs();
        p.copyFiles();
        p.runGradleWrapper();
        p.usage();
        //p.createPathFile();
    }

    void usage() {
        System.out.println("\n\nYou can run your application with:");
        System.out.println("   cd "+path);
        System.out.println("   ./gradlew -q --console=plain\n");
        System.out.println("or (if you have JaCaMo scripts installed)");
        System.out.println("   jacamo "+path+"/"+id+".jcm\n");
        System.out.println("an Eclipse project can be created using");
        System.out.println("   'Existing Gradle Project' from Eclipse menu File/Import\n");
        System.out.println("or");
        System.out.println("   ./gradlew eclipse");
    }
    
    void runGradleWrapper() {
        ProjectConnection connection = GradleConnector
                .newConnector()
                .forProjectDirectory(path)
                .connect();
        connection.newBuild()
            .forTasks("wrapper")
            .run();
        connection.close();
    }

    void createDirs() {
        if (!path.exists()) {
            System.out.println("Creating path "+path);
            path.mkdirs();
        }

        new File(path + "/doc").mkdirs();
        new File(path + "/lib").mkdirs();
        new File(path + "/log").mkdirs();
        new File(path + "/src/agt").mkdirs();
        new File(path + "/src/agt/inc").mkdirs();
        new File(path + "/src/agt/jia").mkdirs();
        new File(path + "/src/env/tools").mkdirs();
        new File(path + "/src/org").mkdirs();
        new File(path + "/src/int").mkdirs();
    }

    void copyFiles() {
        copyFile("project", new File( path+"/"+id+".jcm"));
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
                l = l.replace("<PROJECT_NAME>", id);
                l = l.replace("<PLATFORM>", "");
                l = l.replace("<PROJECT-FILE>", id+".jcm");
                l = l.replace("<PROJECT-FILE>", id+".jcm");
                l = l.replace("<PROJECT-FILE>", id+".jcm");
                l = l.replace("<VERSION>", c.getJaCaMoVersion());
                l = l.replace("<DATE>", new SimpleDateFormat("MMMM dd, yyyy - HH:mm:ss").format(new Date()));

                l = l.replace("<DEFAULT_AGENT>", "agent sample_agent");
                l = l.replace("<AG_NAME>", "sample_agent");

                l = l.replace("<PCK>", "tools");
                l = l.replace("<ARTIFACT_NAME>", "Counter");
                l = l.replace("<SUPER_CLASS>", "Artifact");

                l = l.replace("<ORGANIZATION_NAME>", id);

                if (consoleApp) {
                    l = l.replace("handlers = jason.runtime.MASConsoleLogHandler", "#handlers = jason.runtime.MASConsoleLogHandler");
                    l = l.replace("#handlers= java.util.logging.ConsoleHandler", "handlers= java.util.logging.ConsoleHandler");
                }

                out.append(l+"\n");
                l = in.readLine();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
