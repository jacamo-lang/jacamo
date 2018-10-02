package jacamo.infra;

import jason.infra.InfrastructureFactory;
import jason.infra.MASLauncherInfraTier;
import jason.runtime.RuntimeServices;

public class JaCaMoInfrastructureFactory  implements InfrastructureFactory {

    public MASLauncherInfraTier createMASLauncher() {
        return new JaCaMoMASLauncherAnt();
    }

    public RuntimeServices createRuntimeServices() {
        return new JaCaMoRuntimeServices(JaCaMoLauncher.getRunner());
    }
}
