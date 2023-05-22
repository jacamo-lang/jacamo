package jacamo.infra;

import jacamo.project.JaCaMoProject;
import jacamo.project.parser.JaCaMoProjectParser;
import jacamo.util.Config;
import org.gradle.tooling.GradleConnector;

import java.io.*;
import java.util.Collection;

/**
 * Run a JaCaMo project with gradle
 *
 * @author jomi
 *
 */
public class RunJaCaMoProject {

//    static JaCaMoMASLauncherAnt launcher;

    // Run the parser
    public static void main (String[] args) {

      String name;
      JaCaMoProjectParser parser;
      var project = new JaCaMoProject();
      var justDeps = false;

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
          if ("--deps".equals(arg)) {
              justDeps = true;
          }
      }

      if (args.length == 0) {
          System.out.println(Config.get().getPresentation()+"\n");
          System.out.println("usage must be:");
          System.out.println("      java "+RunJaCaMoProject.class.getName()+" <JaCaMo Project File> [notrun]");
          return;
      } else {
          name = args[0].trim();
          System.err.println("reading from file " + name + " ..." );
          try {
              parser = new JaCaMoProjectParser(new java.io.FileInputStream(name));
          } catch(java.io.FileNotFoundException e){
              System.err.println("file \"" + name + "\" not found.");
              return;
          }
      }

//      boolean       nrunmas = args.length >= 2 && args[1].equals("notrun");
//      if (!nrunmas) nrunmas = args.length >= 3 && args[2].equals("notrun");

//      String task = "run";
//      if (args.length >= 2 && args[1].equals("jar"))
//          task = "jar";

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
              runArgs += args[i].trim() + " ";
          project.setRunArgs(runArgs);

          buildDeps(project);
          if (justDeps) {
              System.out.println("JCM packages depencies updated at .jcm-deps.gradle");
          } else {
              runByGradle(project);
          }

//          solvePackagesByGradle(project);
//          project.movePackagesToClassPath();

//          launcher = (JaCaMoMASLauncherAnt)project.getInfrastructureFactory().createMASLauncher();
//          launcher.setProject(project);
//          launcher.writeScripts(false, false);
//          launcher.setTask(task);

//          if (nrunmas) {
//              System.out.println("To run your MAS, just type \"ant -f bin/"+file.getName().substring(0,file.getName().length()-3)+"xml\"");
//          } else {
//              new Thread(launcher, "MAS-Launcher").start();
//          }
      } catch(Exception e){
          System.err.println("parsing errors found... \n" + e);
      }
    }

    public static void buildDeps(JaCaMoProject project) {
        var directory = new File(project.getDirectory()); //new File("bin/gradle-tmp");
        var jcmDepsFile = new File(directory.getAbsoluteFile()+"/.jcm-deps.gradle");
        copyFile("jcm-deps.gradle", jcmDepsFile, project.getSocName(), project.getPackages().values());
    }

    public static void runByGradle(JaCaMoProject project) {
        var directory = new File(project.getDirectory()); //new File("bin/gradle-tmp");
        var buildGradleFile = new File(directory.getAbsoluteFile()+"/build.gradle");
        if (!buildGradleFile.exists()) {
            System.out.println("no build.gradle exists... creating one");
            copyFile("build.gradle", buildGradleFile, project.getSocName(), project.getPackages().values());
        }
        System.out.println("Starting gradle for "+project.getSocName());

        var connection = GradleConnector
                .newConnector()
                .forProjectDirectory(directory)
                .connect();
        connection.newBuild()
                .setStandardError(System.err)
                .setStandardOutput(System.out)
                .forTasks("run")
                .run();
    }

//    public MASLauncherInfraTier getLauncher() {
//        return launcher;
//    }

    /** find packages using gradle and place them in the project class path */
    /*public static void solvePackagesByGradle(JaCaMoProject project) {
        try {
            // find packages not solved
            var toBeSolved = getUnsolvedPackagesStrings(project);

            if (toBeSolved.isEmpty())
                return;

            // look at cache for packages
            var directory = new File("bin/gradle-tmp");
            directory.mkdirs();

            var cacheFile = new File(directory.getAbsoluteFile()+"/pkgs.cache");
            Properties solvedPkgs = new Properties();
            if (cacheFile.exists()) {
//                System.out.println("Using gradle cache to solve packages "+toBeSolved);

                solvedPkgs.load(new FileReader(cacheFile));
                for (String p: solvedPkgs.stringPropertyNames()) {
                    if (!toBeSolved.keySet().contains(p)) { // ignore the property if not a wanted package
                        continue;
                    }
                    var fileName = solvedPkgs.getProperty(p);
                    var args = toBeSolved.get(p).split(":");
                    if (args.length == 3
                            && fileName.endsWith(".jar")
                            && fileName.contains(args[0])
                            && fileName.contains(args[1])
                            && fileName.contains(args[2])
                        ) {
                        var f = new File(fileName);
                        if (f.exists()) {
//                            System.out.println("solving "+p+" from cache "+f.getAbsolutePath());
                            project.getPackages().put(p,f.getAbsolutePath());
                        }
                    }
                }
                toBeSolved = getUnsolvedPackagesStrings(project);
            }

            if (toBeSolved.isEmpty())
                return;

            System.out.println("Using gradle to find packages "+toBeSolved+"\nit may take a while....");

            // create build.gradle file to download dependencies
            var buildGradleFile = new File(directory.getAbsoluteFile()+"/build.gradle");
            copyFile("build.gradle", buildGradleFile, project.getSocName(), toBeSolved.values());

            ProjectConnection connection = GradleConnector
                    .newConnector()
                    .forProjectDirectory(directory)
                    .connect();

            var eclipseProject = connection.getModel(EclipseProject.class);
            for (EclipseExternalDependency d: eclipseProject.getClasspath()) {
                var m = d.getGradleModuleVersion();
                if (m != null) {
                    var spec = m.getGroup() + ":"
                            + m.getName() + ":"
                            + m.getVersion();
//                    System.out.println(spec + "=" + d.getFile());
                    for (var k: toBeSolved.keySet()) {
                        var v = toBeSolved.get(k);
                        if (v.equals(spec)) {
//                            System.out.println(spec + "=" + d.getFile());
                            solvedPkgs.put(k,d.getFile().getAbsolutePath());
                            project.getPackages().put(k,d.getFile().getAbsolutePath());
                        }
                    }
                }
            }

            connection.close();

            //  store cache
            solvedPkgs.store(new BufferedWriter(new FileWriter(cacheFile)),"cache values previously solved by gradle");

            toBeSolved = getUnsolvedPackagesStrings(project);
            if  (!toBeSolved.isEmpty()) {
                System.out.println("The following packages are not found: "+toBeSolved.values()+"\n");
            }

        } catch (Exception e) {
            System.err.println("Error solving package with gradle"+e);
        }
    }*/

    /*private static Map<String,String> getUnsolvedPackagesStrings(JaCaMoProject project) {
        var toBeSolved = new HashMap<String,String>();
        for (var k: project.getPackages().keySet()) {
            var v = project.getPackages().get(k);
            if (! (new File(v).exists())) {
                if (v.split(":").length == 3) { // it is  gradle style
                    toBeSolved.put(k,v);
                }
            }
        }
        return toBeSolved;
    }*/

    public static void copyFile(String source, File target, String id, Collection<String> toBeSolved) {
        var sTBS = "";
        for (String s: toBeSolved)
            sTBS += "implementation('" + s + "')\n";

        try {
            BufferedReader in = new BufferedReader( new InputStreamReader( Config.get().getDefaultResource(source) ));
            BufferedWriter out = new BufferedWriter(new FileWriter(target));
            String l = in.readLine();
            while (l != null) {
                l = l.replace("<PROJECT_NAME>", id);
                l = l.replace("<PROJECT-FILE>", id+".jcm");
                l = l.replace("<VERSION>", Config.get().getJaCaMoVersion());

                l = l.replace("<DEPENDENCIES>", sTBS);

                out.append(l+"\n");
                l = in.readLine();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
