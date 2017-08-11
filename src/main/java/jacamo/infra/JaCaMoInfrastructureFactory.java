package jacamo.infra;

import jason.infra.InfrastructureFactory;
import jason.infra.MASLauncherInfraTier;
import jason.runtime.RuntimeServicesInfraTier;

public class JaCaMoInfrastructureFactory  implements InfrastructureFactory {

    public MASLauncherInfraTier createMASLauncher() {
        return new JaCaMoMASLauncherAnt();
    }

    public RuntimeServicesInfraTier createRuntimeServices() {
        return new JaCaMoRuntimeServices(JaCaMoLauncher.getRunner());
    }
}
