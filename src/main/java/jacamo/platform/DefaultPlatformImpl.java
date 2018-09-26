package jacamo.platform;

import jacamo.project.JaCaMoProject;

public class DefaultPlatformImpl implements Platform {

    protected JaCaMoProject project;

    @Override
    public void setJcmProject(JaCaMoProject p) {
        this.project = p;       
    }
}
