import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import cartago.AgentIdCredential;
import cartago.ArtifactId;
import cartago.ArtifactInfo;
import cartago.ArtifactObsProperty;
import cartago.CartagoEnvironment;
import cartago.CartagoEvent;
import cartago.CartagoException;
import cartago.ICartagoCallback;
import cartago.ICartagoContext;
import cartago.Op;
import cartago.Workspace;
import jacamo.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkspaceCreationTest {

    @BeforeClass
    public static void launchSystem() {
        TestUtils.launchSystem("src/test/java/project/p5.jcm");
    }

    @AfterClass
    public static void stopSystem() {
        TestUtils.stopSystem();
    } 

    @Test
    public void test101GetWorkspaces() {
        System.out.println("Testing if workspaces from jcm were really created.");
        Workspace main = CartagoEnvironment.getInstance().getRootWSP().getWorkspace();
        assertTrue(main.getChildWSP("w1") != null);
        assertTrue(main.getChildWSP("w2") != null);
        assertTrue(main.getChildWSP("w5") != null);
        
        //System.out.println(main.getChildWSP("w1").get().getId().getFullName());
    }
    
    @Test
    public void test201PostProperty() throws Exception {
        System.out.println("Testing get observable property.");
        
        Object[] props = getObsPropValue("/main/w1", "c1", "count");
        System.out.println("counter initial value: "+((Integer)props[0]).intValue());
        assertEquals( 10, ((Integer)props[0]).intValue(), 0 );
        
        execOp("/main/w1", "c1", "inc", null);
        
        props = getObsPropValue("/main/w1", "c1", "count");
        System.out.println("counter after inc: "+((Integer)props[0]).intValue());
        assertEquals( 11, ((Integer)props[0]).intValue(), 0 );
    }
    
    public Object[] getObsPropValue(String wrksName, String artName, String obsPropId) throws CartagoException {
        ArtifactInfo info = CartagoEnvironment.getInstance().getController(wrksName).getArtifactInfo(artName);
        for (ArtifactObsProperty op : info.getObsProperties()) {
            if (op.getName().equals(obsPropId)) {
                return op.getValues();
            }           
        }
        return null;
    }
    
    public void execOp(String wrksName, String artName, String operation, Object[] values) throws CartagoException, InterruptedException {
        Workspace w = CartagoEnvironment.getInstance().resolveWSP(wrksName).getWorkspace();
        ArtifactId aid = w.getArtifact(artName);
        if (aid == null) {
            throw new CartagoException("artifact "+artName+" not found");
        }
        ICartagoContext ctx = w.joinWorkspace(new AgentIdCredential("test"), new ICartagoCallback() {
            public void notifyCartagoEvent(CartagoEvent arg0) {         }
        });
        if (values != null) {
            ctx.doAction(1, new Op(operation, values), null, -1);
        } else {
            ctx.doAction(1, new Op(operation), null, -1);
            Thread.sleep(100);
        }
    }
    
}
