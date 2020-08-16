package jacamo.util;

import java.util.concurrent.atomic.AtomicBoolean;

import jacamo.infra.JaCaMoLauncher;
import jason.JasonException;

public class TestUtils {
    protected static AtomicBoolean  systemLaunched = new AtomicBoolean(false);
    
    public static boolean launchSystem(String jcm) {
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
                while (JaCaMoLauncher.getJaCaMoRunner() == null) {
                    System.out.println("waiting for the runner...");
                    Thread.sleep(200);
                }
                // wait for start to finish
                while (!JaCaMoLauncher.getJaCaMoRunner().isRunning()) {
                    System.out.println("waiting for jcm to start...");
                    Thread.sleep(200);
                }
                
                // wait some agent to become idle
                if (!JaCaMoLauncher.getJaCaMoRunner().getAgs().isEmpty()) {
                    while (JaCaMoLauncher.getJaCaMoRunner().getAgs().values().iterator().next().getCycleNumber() < 2) {
                        System.out.println("waiting agent to start...");
                        Thread.sleep(200);                      
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(() -> stopSystem()));
            return true;
        } else {
            return false;
        }
    }
    
    public static void stopSystem() {
        if (systemLaunched.getAndSet(false)) {
            int i=0;
            JaCaMoLauncher.getJaCaMoRunner().finish(0, false, 0); // do not stop the JVM
            while (JaCaMoLauncher.getRunner().isRunning() && i++ < 20) {
                System.out.println("waiting for jacamo to stop ....");
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (i < 20)
                System.out.println("JaCaMo stopped");
        }
    }

}
