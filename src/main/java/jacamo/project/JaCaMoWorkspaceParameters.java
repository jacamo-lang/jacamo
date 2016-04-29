package jacamo.project;

import jason.mas2j.AgentParameters;
import jason.mas2j.ClassParameters;

import java.util.HashMap;
import java.util.Map;


public class JaCaMoWorkspaceParameters {
    
    protected JaCaMoProject project;
    protected String name;
    protected String node;
    protected Map<String,ClassParameters> artifacts = new HashMap<String, ClassParameters>();
    protected boolean debug = false;

    public JaCaMoWorkspaceParameters(JaCaMoProject project) {
        this.project = project;
    }
    
    public void setName(String n) { name = n; }
    public String getName()       { return name; }
    
    public void setNode(String h) { node = h; }
    public String getNode()       { return node; }

    public void addArtifact(String name, ClassParameters className) {
        artifacts.put(name,className);
    }
    
    public Map<String,ClassParameters> getArtifacts() {
        return artifacts;
    }
    
    public ClassParameters getArtifact(String artId) {
        return artifacts.get(artId);
    }

    public void setDebug(boolean on) {
        debug = on;
    }
    public boolean hasDebug() {
        return debug;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("   workspace "+name+" {\n");
        for (String a: artifacts.keySet()) {
            s.append("      artifact "+a+": "+artifacts.get(a)+"\n");
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

        if (hasDebug())
            s.append("\n      debug\n");
        s.append("   }");
        return s.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof JaCaMoWorkspaceParameters)) return false;
        return this.name.equals( ((JaCaMoWorkspaceParameters)o).name );
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
