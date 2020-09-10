package jacamo.infra;

import java.io.File;

import jacamo.project.JaCaMoProject;
import jacamo.project.parser.JaCaMoProjectParser;
import jacamo.util.Config;
import jason.infra.MASLauncherInfraTier;

/**
 * Run a JaCaMo project
 *
 * parameters:
 *    /JaCaMo Project File/ [Deployment File] [norun]
 *
 * "notrun" means that only the ant script will be create and the system will not start running
 *
 * @author jomi
 *
 */
public class RunJaCaMoProject {

    static JaCaMoMASLauncherAnt launcher;

    // Run the parser
    public static void main (String args[]) {

      String name;
      JaCaMoProjectParser parser;
      JaCaMoProject project = new JaCaMoProject();

      if (args.length == 0) {
          System.out.println(Config.get().getPresentation()+"\n");
          System.out.println("usage must be:");
          System.out.println("      java "+RunJaCaMoProject.class.getName()+" <JaCaMo Project File> [notrun]");
          return;
      } else {
          name = args[0];
          System.err.println("reading from file " + name + " ..." );
          try {
              parser = new JaCaMoProjectParser(new java.io.FileInputStream(name));
          } catch(java.io.FileNotFoundException e){
              System.err.println("file \"" + name + "\" not found.");
              return;
          }
      }

      boolean       nrunmas = args.length >= 2 && args[1].equals("notrun");
      if (!nrunmas) nrunmas = args.length >= 3 && args[2].equals("notrun");

      String task = "run";
      if (args.length >= 2 && args[1].equals("jar"))
          task = "jar";

      // parsing
      try {
          File file = new File(name);
          File directory = file.getAbsoluteFile().getParentFile();
          project = parser.parse(directory.toString());
          if (Config.get().getJaCaMoHome().isEmpty()) {
              //if (Config.get().getUserConfFile().exists())
              //    System.out.println("JaCaMo is not configured, creating a default configuration.");
              //else
              Config.get().setShowFixMsgs(false);
              Config.get().fix();
          }
          project.setProjectFile(file);
          System.out.println("file "+name+" parsed successfully!\n");

          String runArgs = "";
          for (int i=1; i<args.length; i++) // do not include the name of the project
              runArgs += args[i] + " ";
          project.setRunArgs(runArgs);
          
          launcher = (JaCaMoMASLauncherAnt)project.getInfrastructureFactory().createMASLauncher();
          launcher.setProject(project);
          launcher.writeScripts(false, false);
          launcher.setTask(task);

          if (nrunmas) {
              System.out.println("To run your MAS, just type \"ant -f bin/"+file.getName().substring(0,file.getName().length()-3)+"xml\"");
          } else {
              new Thread(launcher, "MAS-Launcher").start();
          }
      } catch(Exception e){
          System.err.println("parsing errors found... \n" + e);
      }
    }

    public MASLauncherInfraTier getLauncher() {
        return launcher;
    }

}
