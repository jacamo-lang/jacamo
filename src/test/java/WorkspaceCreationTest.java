import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import cartago.AgentIdCredential;
import cartago.ArtifactId;
import cartago.ArtifactInfo;
import cartago.ArtifactObsProperty;
import cartago.CartagoContext;
import cartago.CartagoException;
import cartago.CartagoService;
import cartago.Op;
import cartago.WorkspaceId;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkspaceCreationTest {

    @BeforeClass
    public static void launchSystem() {
        TestUtils.launchSystem("src/test/java/project/p5.jcm");
    }
    
    @Test
    public void test101GetWorkspaces() {
        System.out.println("Testing if workspaces from jcm were really created.");
        Collection<String> workspaces = CartagoService.getNode().getWorkspaces();
        System.out.println("Existing workspaces: "+workspaces);
        
        assertTrue(workspaces.contains("w1"));
        assertTrue(workspaces.contains("w2"));
        assertTrue(workspaces.contains("w5"));
    }
    
    @Test
    public void test201PostProperty() {
        System.out.println("Testing get observable property.");
        
        try {
            Object[] props = getObsPropValue("w1", "c1", "count");
            System.out.println("counter initial value: "+((Integer)props[0]).intValue());
            assertEquals( 10, ((Integer)props[0]).intValue(), 0 );
            
            execOp("w1", "c1", "inc", null);
            
            props = getObsPropValue("w1", "c1", "count");
            System.out.println("counter after inc: "+((Integer)props[0]).intValue());
            assertEquals( 11, ((Integer)props[0]).intValue(), 0 );
        } catch (CartagoException e) {
            e.printStackTrace();
        }
    }
    
    public Object[] getObsPropValue(String wrksName, String artName, String obsPropId) throws CartagoException {
        ArtifactInfo info = CartagoService.getController(wrksName).getArtifactInfo(artName);
        for (ArtifactObsProperty op : info.getObsProperties()) {
            if (op.getName().equals(obsPropId)) {
                return op.getValues();
            }           
        }
        return null;
    }
    Map<String, CartagoContext> contexts = new HashMap<>();

    public void execOp(String wrksName, String artName, String operation, Object[] values) throws CartagoException {
        CartagoContext ctxt = getContext(wrksName);
        ArtifactId aid = ctxt.lookupArtifact(getWId(wrksName), artName);
        if (aid == null) {
            throw new CartagoException("artifact "+artName+" not found");
        }
        if (values != null) 
            ctxt.doAction(aid, new Op(operation, values));
        else
            ctxt.doAction(aid, new Op(operation));
    }
    
    protected CartagoContext getContext(String wrksName) throws CartagoException {
        CartagoContext ctxt = contexts.get(wrksName);
        if (ctxt == null) {
            ctxt = CartagoService.startSession(wrksName, new AgentIdCredential("restapi_"+wrksName));
            contexts.put(wrksName, ctxt);
            ctxt.joinWorkspace( wrksName );
        }
        return ctxt;
    }
    
    protected WorkspaceId getWId(String wrksName) throws CartagoException {
        return getContext(wrksName).getJoinedWspId(wrksName); 
    }
}
