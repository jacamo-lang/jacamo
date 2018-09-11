package jacamo.platform;

import jacamo.util.Config;

public class AgentWebInspector extends DefaultPlatform {

    @Override
    public void init(String[] args) {
        if (args.length == 1) {
            Config.get().setProperty(Config.START_WEB_MI, args[0]);
        }
    }
}
