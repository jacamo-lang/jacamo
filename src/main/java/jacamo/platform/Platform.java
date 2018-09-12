package jacamo.platform;

import jacamo.project.JaCaMoProject;

public interface Platform {
    public void init(String[] args) throws Exception;
    public void setJcmProject(JaCaMoProject p);
    public void start();
    public void stop();
}
