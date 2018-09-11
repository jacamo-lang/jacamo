package jacamo.platform;

import jacamo.project.JaCaMoProject;

public interface Platform {
    public void init(String[] args);
    public void setJcmProject(JaCaMoProject p);
    public void start();
    public void stop();
}
