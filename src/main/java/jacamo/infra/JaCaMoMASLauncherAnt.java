package jacamo.infra;

import java.io.File;

import jacamo.project.JaCaMoProject;
import jacamo.util.Config;
import jason.infra.centralised.CentralisedMASLauncherAnt;

/**
 * Write the Ant script to run the MAS in JaCaMo infrastructure
 *
 * @author Jomi
 */
public class JaCaMoMASLauncherAnt extends CentralisedMASLauncherAnt {

    protected String replaceMarks(String script, boolean debug) {
        script = replace(script, "<PROJECT-RUNNER-CLASS>", JaCaMoLauncher.class.getName());
        script = replace(script, "<PROJECT-RUNNER-CLASS>", JaCaMoLauncher.class.getName());
        script = replace(script, "<VERSION>", Config.get().getJaCaMoVersion());

        String name = project.getProjectFile().getName();
        script = replace(script, "<PROJECT-FILE-NOEXT>", name.substring(0,name.length()-4));
        script = replace(script, "<PROJECT-FILE-NOEXT>", name.substring(0,name.length()-4));
        script = replace(script, "<PROJECT-FILE-NOEXT>", name.substring(0,name.length()-4));

        String lib = "";
        /*lib += "        <pathelement location=\""+Config.get().getJaCaMoHome()+"/lib/cartago.jar\"/>\n";
        lib += "        <pathelement location=\""+Config.get().getJaCaMoHome()+"/lib/c4jason.jar\"/>\n";
        lib += "        <pathelement location=\""+Config.get().getJaCaMoHome()+"/lib/moise.jar\"/>\n";
        lib += "        <pathelement location=\""+Config.get().getJaCaMoHome()+"/lib/jacamo.jar\"/>\n";
        lib += "        <pathelement location=\""+Config.get().getJaCaMoHome()+"/lib/npl.jar\"/>\n";

        if (project.isJade()) {
            lib += "        <pathelement location=\""+Config.get().getJaCaMoHome()+"/lib/jade.jar\"/>\n";
        }*/

        if (new File(Config.get().getJaCaMoHome() + File.separator + "lib").exists()) {
            lib += "        <fileset dir=\""+Config.get().getJaCaMoHome()+"/lib\" > <include name=\"*.jar\" /> </fileset>\n";
        } else {
            lib += "        <fileset dir=\""+Config.get().getJaCaMoHome()+"/libs\" > <include name=\"*.jar\" /> </fileset>\n";
        }

        script = replace(script, "<PATH-LIB>", lib + "\n<PATH-LIB>");

        // build javac entry
        StringBuilder jc = new StringBuilder();
        for (String p: ((JaCaMoProject)project).getJavaPaths().getPaths()) {
            if (p.startsWith("file:"))
                p = p.substring(5);
            jc.append("<javac srcdir=\"${basedir}/"+p+"\" destdir=\"${build.dir}\" debug=\"true\" optimize=\"true\" includeantruntime=\"false\" >\n");
            jc.append("            <classpath refid=\"project.classpath\"/>\n");
            jc.append("        </javac>\n        ");
        }
        
        script = replace(script, "<JAVAC>", jc.toString());

        String runArgs = ((JaCaMoProject)project).getRunArgs();
        if (runArgs != null)
            script = replace(script, "<RUN-ARGS>", runArgs);

        return super.replaceMarks(script, debug);
    }

}
