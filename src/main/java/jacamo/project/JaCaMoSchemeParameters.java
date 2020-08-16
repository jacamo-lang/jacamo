package jacamo.project;




public class JaCaMoSchemeParameters extends JaCaMoOrgParameters {
    private static final long serialVersionUID = 1L;

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
            s.append("         debug: "+getDebugConf()+"\n");
        }
        s.append("      }");
        return s.toString();
    }

}
