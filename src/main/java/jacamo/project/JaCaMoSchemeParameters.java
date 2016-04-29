package jacamo.project;




public class JaCaMoSchemeParameters extends JaCaMoOrgParameters {
    
    protected String type;
    
    public JaCaMoSchemeParameters(JaCaMoProject project) {
        super(project);
    }
    
    public void setType(String t) { type = t; }
    public String getType()       { return type; }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("      scheme "+name+": "+type);
        s.append(" {\n");
        for (String a: parameters.keySet()) {
            s.append("         "+a+": "+parameters.get(a)+"\n");
        }
        if (hasDebug()) {
            s.append("         debug\n");            
        }
        s.append("      }");
        return s.toString();
    }
    
}
