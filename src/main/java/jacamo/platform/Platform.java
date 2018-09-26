package jacamo.platform;

import jacamo.project.JaCaMoProject;

public interface Platform { 
    default public void init(String[] args) throws Exception {}
    default public void setJcmProject(JaCaMoProject p) {}
    default public void start() {}
    default public void stop() {}
}
