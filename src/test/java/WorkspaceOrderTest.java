import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jacamo.project.JaCaMoAgentParameters;
import jacamo.project.JaCaMoProject;

/** JUnit test case for syntax package */
public class WorkspaceOrderTest {
    
    @Test
    public void testOrder() {
        JaCaMoProject p = new JaCaMoProject();
        JaCaMoAgentParameters ap = new JaCaMoAgentParameters(p);
        ap.addWorkspace("w2", "n4");
        ap.addWorkspace("w1", null);
        ap.addWorkspace("w4", null);
        ap.addWorkspace("w3", "n5");
        p.addNodeHost("n5", "x.com", true);
        
        assertEquals("[w1, w2, w4, w3]", ap.getWorkspaces().toString());
    }

}
