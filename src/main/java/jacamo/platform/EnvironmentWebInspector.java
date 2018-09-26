package jacamo.platform;

import cartago.CartagoService;
import jacamo.util.Config;

public class EnvironmentWebInspector implements Platform {
    
    boolean webOn = true;

    @Override
    public void init(String[] args) {
        if (args.length == 1) {
            Config.get().setProperty(Config.START_WEB_EI, args[0]);
            webOn = !"false".equals(args[0]);
        }
    }

    @Override
    public void start() {
        if (webOn) {
            EnvironmentInspectorWeb.startHttpServer();
            EnvironmentInspectorWeb.registerWorkspace(CartagoService.MAIN_WSP_NAME);
        }
    }
}
