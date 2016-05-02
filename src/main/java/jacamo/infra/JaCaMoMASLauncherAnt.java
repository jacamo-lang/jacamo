package jacamo.infra;

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

        String lib = "";
        /*lib += "        <pathelement location=\""+Config.get().getJaCaMoHome()+"/lib/cartago.jar\"/>\n";
        lib += "        <pathelement location=\""+Config.get().getJaCaMoHome()+"/lib/c4jason.jar\"/>\n";            
        lib += "        <pathelement location=\""+Config.get().getJaCaMoHome()+"/lib/moise.jar\"/>\n";
        lib += "        <pathelement location=\""+Config.get().getJaCaMoHome()+"/lib/jacamo.jar\"/>\n";            
        lib += "        <pathelement location=\""+Config.get().getJaCaMoHome()+"/lib/npl.jar\"/>\n";            

        if (project.isJade()) {
            lib += "        <pathelement location=\""+Config.get().getJaCaMoHome()+"/lib/jade.jar\"/>\n";                        
        }*/
        lib += "        <fileset dir=\""+Config.get().getJaCaMoHome()+"/lib\" >  <include name=\"*.jar\" /> </fileset>\n";
        
        script = replace(script, "<PATH-LIB>", lib + "\n<PATH-LIB>");
                
        //script = replace(script, "<OTHER-TASK>", startContainers);

        return super.replaceMarks(script, debug);
    }

}
