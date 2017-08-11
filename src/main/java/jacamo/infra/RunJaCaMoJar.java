package jacamo.infra;

import java.io.File;

import jacamo.project.JaCaMoProject;
import jacamo.project.parser.JaCaMoProjectParser;
import jacamo.util.Config;
import jason.infra.MASLauncherInfraTier;

/**
 * Run a JaCaMo jar tasks
 *
 * parameters:
 *    /JaCaMo Project File/ application
 *
 * @author jomi
 *
 */
public class RunJaCaMoJar {

    static JaCaMoMASLauncherAnt launcher;

    // Run the parser
    public static void main (String args[]) {

      String name;
      JaCaMoProjectParser parser;
      JaCaMoProject project = new JaCaMoProject();

      if (args.length == 0) {
          System.out.println(Config.get().getPresentation()+"\n");
          System.out.println("usage must be:");
          System.out.println("      java "+RunJaCaMoJar.class.getName()+" <JaCaMo Project File.jcm> [application]");
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

      String task = "run";
      if (args[1].equals("application"))
          task = "jar";
      else {
          System.out.println(args[1]+" is not implemented");
          System.exit(1);
      }

      // parsing
      try {
          File file = new File(name);
          File directory = file.getAbsoluteFile().getParentFile();
          project = parser.parse(directory.toString());
          if (Config.get().getJaCaMoHome().isEmpty()) {
              System.out.println("JaCaMo is not configured, creating a default configuration.");
              Config.get().fix();
          }
          project.setProjectFile(file);
          System.out.println("file "+name+" parsed successfully!\n");

          launcher = (JaCaMoMASLauncherAnt)project.getInfrastructureFactory().createMASLauncher();
          launcher.setProject(project);
          launcher.writeScripts(false, false);
          launcher.setTask(task);

          new Thread(launcher, "MAS-Launcher").start();
      } catch(Exception e){
          System.err.println("parsing errors found... \n" + e);
      }
    }

    public MASLauncherInfraTier getLauncher() {
        return launcher;
    }

}
