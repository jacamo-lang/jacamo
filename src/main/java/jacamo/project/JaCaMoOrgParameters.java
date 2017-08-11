package jacamo.project;

import jason.mas2j.AgentParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JaCaMoOrgParameters extends JaCaMoWorkspaceParameters {

    protected List<JaCaMoGroupParameters>  groups     = new ArrayList<JaCaMoGroupParameters>();
    protected List<JaCaMoSchemeParameters> schemes    = new ArrayList<JaCaMoSchemeParameters>();
    protected Map<String,String>           parameters = new HashMap<String, String>(); // like source ....
    protected String                       debug      = null;

    public JaCaMoOrgParameters(JaCaMoProject project) {
        super(project);
    }

    public void addParameter(String k, String v) {
        parameters.put(k, v);
    }
    public String getParameter(String k) {
        return parameters.get(k);
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
        if (getNode() != null)
            s.append("      node: "+getNode()+"\n");
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
