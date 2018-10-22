package jacamo.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JaCaMoInstParameters extends JaCaMoWorkspaceParameters {

    protected List<String>  wrks     = new ArrayList<>();
    protected Map<String,String>  parameters = new HashMap<>(); // like source ....

    public JaCaMoInstParameters(JaCaMoProject project) {
        super(project);
    }

    public void addParameter(String k, String v) {
        parameters.put(k, v);
    }
    public String getParameter(String k) {
        return parameters.get(k);
    }

    public void addWorkspace(String wId) {
        wrks.add(wId);
    }
    public List<String> getWorkspaces() {
        return wrks;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("   institution "+name);

        s.append(" : "+getParameter("source"));
        s.append(" {\n");

        for (String k: parameters.keySet()) {
            if (! k.equals("source"))
                s.append("      "+k+": "+parameters.get(k)+"\n");
        }
        if (!wrks.isEmpty()) {
            s.append("      workspaces: ");
            String v = "";
            for (String w: wrks) {
                s.append(v+w);
                v = ", ";
            }
        }
        if (hasDebug())
            s.append("\n      debug\n");
        s.append("\n   }");
        return s.toString();
    }

}
