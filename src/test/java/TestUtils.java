
import java.util.concurrent.atomic.AtomicBoolean;

import jacamo.infra.JaCaMoLauncher;
import jason.JasonException;

public final class TestUtils {
    static AtomicBoolean  systemLaunched = new AtomicBoolean(false);
    static JaCaMoLauncher runner = null;
    
    public static void launchSystem(String jcm) {
        if (!systemLaunched.getAndSet(true)) {
            try {
                JaCaMoLauncher.setDefaultLogProperties("/templates/logging-console.properties");
                // Launch jacamo running some jcm
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
                    runner = JaCaMoLauncher.getJaCaMoRunner();
                    Thread.sleep(200);
                }
                // wait for start to finish
                while (!runner.hasStartFinished()) {
                    System.out.println("waiting for jcm to start...");
                    Thread.sleep(200);
                }
                
                // wait some agent to become idle
                if (!runner.getAgs().isEmpty()) {
                    while (! runner.getAgs().values().iterator().next().canSleep()) {
                        System.out.println("waiting agent to sleep...");
                        Thread.sleep(200);                      
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(() -> stopSystem()));
        }
    }
    
    public static void stopSystem() {
        runner.finish(0, false); // do not stop the JVM
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
