package jacamo.project;

import java.util.ArrayList;
import java.util.List;

import jason.mas2j.AgentParameters;



public class JaCaMoGroupParameters extends JaCaMoOrgParameters {
    
    protected String type;
    protected List<JaCaMoGroupParameters>  subGroups = new ArrayList<JaCaMoGroupParameters>();
    
    public JaCaMoGroupParameters(JaCaMoProject project) {
        super(project);
    }
    
    public void setType(String t) { type = t; }
    public String getType()       { return type; }
    
    public void addSubGroup(JaCaMoGroupParameters sg) {
        subGroups.add(sg);
    }
    public List<JaCaMoGroupParameters> getSubGroups() {
        return subGroups;
    }
    
    
    public JaCaMoGroupParameters find(String gId) {
        if (this.getName().equals(gId))
            return this;
        for (JaCaMoGroupParameters sg: subGroups) {
            JaCaMoGroupParameters sgf = sg.find(gId);
            if (sgf != null)
                return sgf;
        }
        return null;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("      group "+name+": "+type);
        s.append(" {\n");
        for (String a: parameters.keySet()) {
            s.append("         "+a+": "+parameters.get(a)+"\n");
        }
        if (hasDebug()) {
            s.append("         debug\n");            
        }
        String bgn = "         players: ";
        for (AgentParameters ap: project.getAgents()) {
            JaCaMoAgentParameters jap = (JaCaMoAgentParameters)ap;
            for (String[] r: jap.getRoles()) {
                if (r[1].equals(this.name)) {
                    s.append(bgn+ap.getAgName()+" "+r[2]+"\n");
                    bgn = "                  ";
                }
            }
        }
        for (JaCaMoGroupParameters sg: subGroups) {
            s.append("\n"+sg+"\n");
        }
        s.append("      }");
        return s.toString();
    }
    
}
