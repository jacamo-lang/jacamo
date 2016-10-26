package jacamo.infra;

import jacamo.project.JaCaMoProject;
import jacamo.project.parser.JaCaMoProjectParser;
import jacamo.util.Config;
import jason.infra.MASLauncherInfraTier;

import java.io.File;

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
    
    static MASLauncherInfraTier launcher;
    
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
      
      // parsing
      try {
          File file = new File(name);
          File directory = file.getAbsoluteFile().getParentFile();
          project = parser.parse(directory.toString());
          //Config.get().fix();
          project.setProjectFile(file);
          System.out.println("file "+name+" parsed successfully!\n");
          
          launcher = project.getInfrastructureFactory().createMASLauncher();
          launcher.setProject(project);
          launcher.writeScripts(false, false);
          
          if (nrunmas) {
              System.out.println("To run your MAS, just type \"ant -f bin/build.xml\"");
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
