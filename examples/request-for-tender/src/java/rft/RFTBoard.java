// CArtAgO artifact code for project request-for-tender

package rft;

import cartago.Artifact;
import cartago.ArtifactConfig;
import cartago.OPERATION;
import cartago.OpFeedbackParam;

public class RFTBoard extends Artifact {

    private int taskId;

    void init() {
        taskId = 0;
    }

    @OPERATION void announce(String terms, String conditions, int deadline, String grArtName, String schArtName, OpFeedbackParam<String> id){
        taskId++;
        try {
            String artifactName = terms+"_bid_board_"+taskId;
            makeArtifact(artifactName, "rft.RFTBidBoard", new ArtifactConfig(terms, conditions, deadline, grArtName, schArtName, getCurrentOpAgentId().getAgentName()));
            defineObsProperty("task", terms, artifactName);
            id.set(artifactName);
        } catch (Exception ex){
            failed("announce_failed");
        }
    }

    @OPERATION void clear(String id){
        String artifactName = "bid_board_"+taskId;
        this.removeObsPropertyByTemplate("task", null, artifactName);
    }
}

