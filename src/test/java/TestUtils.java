
import jacamo.infra.JaCaMoLauncher;
import jason.JasonException;
import jason.infra.centralised.BaseCentralisedMAS;

public final class TestUtils {
    static Boolean systemRunning = false;
    static BaseCentralisedMAS runner = null;
    
    public static void launchSystem(String jcm) {
        if (!systemRunning) {
            try {
                // Launch jacamo and jacamo-rest running test0.jcm
                new Thread() {
                    public void run() {
                        String[] arg = { jcm };
                        try {
                            JaCaMoLauncher.main(arg);
                        } catch (JasonException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                // get runner
                while (runner == null) {
                    System.out.println("waiting for the runner...");
                    runner = JaCaMoLauncher.getRunner();
                    Thread.sleep(200);
                }
                // wait for agents
                while (JaCaMoLauncher.getRunner().getNbAgents() == 0) {
                    System.out.println("waiting for agents to start...");
                    Thread.sleep(200);
                }
                Thread.sleep(600);
            } catch (Exception e) {
                e.printStackTrace();
            }
            systemRunning = true;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> stopSystem()));
        }
    }
    
    public static void stopSystem() {
        JaCaMoLauncher.getRunner().finish(0, false); // do not stop the JVM
        while (JaCaMoLauncher.getRunner() != null) {
            System.out.println("waiting for jacamo to STOP ....");
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("JaCaMo stopped");
    }

}
