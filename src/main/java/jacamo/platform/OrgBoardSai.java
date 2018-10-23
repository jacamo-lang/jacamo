package jacamo.platform;

import java.util.logging.Level;
import java.util.logging.Logger;

import cartago.ArtifactId;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import cartago.OperationException;
import moise.common.MoiseException;
import npl.parser.ParseException;
import ora4mas.nopl.OrgBoard;

// TODO: move to sai package
public class OrgBoardSai extends OrgBoard {

    protected Logger logger = Logger.getLogger(OrgBoardSai.class.getName());
    
    protected String institution = null;
    protected Object saiEngine   = null;

    @OPERATION
    public void setInstitution(String instId, Object instAId) throws ParseException, MoiseException, OperationException {
        try {
            // get listener object from SAI
            OpFeedbackParam<Object> fbre = new OpFeedbackParam<>();
            execLinkedOp((ArtifactId)instAId, "getSaiEngine", fbre);
    
            this.institution = instId;
            this.saiEngine   = fbre.get();
            defineObsProperty("institution", instId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getGroupBoardClass() {
        // TODO: use get class name
        return "sai.bridges.jacamo.GroupBoardSai";
    }
    
    @Override
    protected void grPostCreation(String id, ArtifactId artId) {
        setInstitution(id, artId);
    }

    @Override
    protected String getSchemeBoardClass() {
        // TODO: use get class name
        return "sai.bridges.jacamo.SchemeBoardSai";
    }

    @Override
    protected void schPostCreation(String id, ArtifactId artId) {
        setInstitution(id, artId);
    }

    protected void setInstitution(String id, ArtifactId artId) {
        try {
            execLinkedOp(artId, "setInstitution", saiEngine );
        } catch (OperationException e) {
            logger.log(Level.WARNING, "Error setting institution "+id+" for "+getId(), e);
        }
    }

}
