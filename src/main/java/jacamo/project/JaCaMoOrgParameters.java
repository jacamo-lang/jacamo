package jacamo.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jason.mas2j.AgentParameters;


public class JaCaMoOrgParameters extends JaCaMoWorkspaceParameters {

    private static final long serialVersionUID = 1L;
    
    protected List<JaCaMoGroupParameters>  groups     = new ArrayList<>();
    protected List<JaCaMoSchemeParameters> schemes    = new ArrayList<>();
    protected Map<String,String>           parameters = new HashMap<>(); // like source ....
    protected String                       debug      = null;
    
    protected String                       institutions = null;

    public JaCaMoOrgParameters(JaCaMoProject project) {
        super(project);
    }

    public void addParameter(String k, String v) {
        parameters.put(k, v);
    }
    public String getParameter(String k) {
        return parameters.get(k);
    }
    
    public void setInstitution(String i) {
        institutions = i;
    }
    public String getInstitution() {
        return institutions;
    }
    public boolean hasInstitution() {
        return institutions != null && !institutions.isEmpty();
    }

    public void addGroup(JaCaMoGroupParameters g) {
        groups.add(g);
    }
    public List<JaCaMoGroupParameters> getGroups() {
        return groups;
    }

    public JaCaMoGroupParameters getGroup(String id) {
        for (JaCaMoGroupParameters g: groups) {
            JaCaMoGroupParameters gg = g.find(id);
            if (gg != null) {
                return gg;
            }
        }
        return null;
    }

    public void addScheme(JaCaMoSchemeParameters s) {
        schemes.add(s);
    }
    public List<JaCaMoSchemeParameters> getSchemes() {
        return schemes;
    }
    public JaCaMoSchemeParameters getScheme(String id) {
        for (JaCaMoSchemeParameters s: schemes) {
            if (s.getName().equals(id))
                return s;
        }
        return null;
    }

    public void setDebug(String arg) {
        debug = arg;
    }
    public boolean hasDebug() {
        return debug != null;
    }
    public String getDebugConf() {
        return debug;
    }



    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("   organisation "+name);

        s.append(" : "+getParameter("source"));
        s.append(" {\n");

        for (String k: parameters.keySet()) {
            if (! k.equals("source"))
                s.append("      "+k+": "+parameters.get(k)+"\n");
        }
        String bgn = "      agents: ";
        for (AgentParameters ap: project.getAgents()) {
            JaCaMoAgentParameters jap = (JaCaMoAgentParameters)ap;
            for (String w: jap.getWorkspaces()) {
                if (w.equals(this.name)) {
                    s.append(bgn+ap.getAgName());
                    bgn = ", ";
                }
            }
        }
        s.append("\n");
        for (JaCaMoGroupParameters g: groups) {
            s.append(g+"\n");
        }
        s.append("\n");
        for (JaCaMoSchemeParameters sch: schemes) {
            s.append(sch+"\n");
        }
        s.append("\n   }");
        return s.toString();
    }

}
