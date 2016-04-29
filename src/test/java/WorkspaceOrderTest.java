import jacamo.project.JaCaMoAgentParameters;
import jacamo.project.JaCaMoProject;
import junit.framework.TestCase;

/** JUnit test case for syntax package */
public class WorkspaceOrderTest extends TestCase {
    
    public void testOrder() {
        JaCaMoProject p = new JaCaMoProject();
        JaCaMoAgentParameters ap = new JaCaMoAgentParameters(p);
        ap.addWorkspace("w2", "n4");
        ap.addWorkspace("w1", null);
        ap.addWorkspace("w4", null);
        ap.addWorkspace("w3", "n5");
        p.addNodeHost("n5", "x.com", true);
        
        System.out.println(ap.getWorkspaces());
    }

}
