// CArtAgO artifact code for project request-for-tender

package rft;

import java.util.ArrayList;
import java.util.List;

import c4jason.ToProlog;
import cartago.AgentId;
import cartago.Artifact;
import cartago.INTERNAL_OPERATION;
import cartago.OPERATION;
import cartago.OpFeedbackParam;

public class RFTBidBoard extends Artifact {

    List<Bid> bids = new ArrayList<Bid>();

    void init(String terms, String conditions, int deadline, String grArtName, String schArtName, String owner) {
        defineObsProperty("terms", terms);
        defineObsProperty("conditions", conditions);
        defineObsProperty("deadline", deadline);
        defineObsProperty("requester", owner);
        defineObsProperty("group", grArtName);
        defineObsProperty("scheme", schArtName);
        defineObsProperty("state", "open");
        execInternalOp("checkDeadline",deadline);
    }

    @INTERNAL_OPERATION void checkDeadline(long dt) {
        await_time(dt);
        signal("list_of_bids", bids.toString()); // TODO: why do I need to send the string and the list does not work?
        getObsProperty("state").updateValue("closed");
    }

    @OPERATION void getBids(OpFeedbackParam<String> b) {
        if (getObsProperty("requester").getValue().equals(getCurrentOpAgentId().getAgentName())) {
            b.set(bids.toString());
        } else {
            failed(getCurrentOpAgentId().getAgentName()+" is not allowed to get the bids! Only "+getObsProperty("requester").getValue()+" is.");
        }
    }

    @OPERATION void notify(String winner) {
        for (Bid d: bids) {
            if (d.tender.equals(winner))
                signal(d.agid, "won");
            else
                signal(d.agid, "lost");
        }
    }

    // TODO: how to receive a list of member from jason?
    @OPERATION void bid(double value, String members, String properties) {
        bids.add(new Bid(getCurrentOpAgentId().getAgentName(),value,members,properties,getOpUserId()));
    }

}

class Bid implements ToProlog {
    String tender;
    double vl;
    String members;
    String props;
    AgentId agid;

    public Bid(String t, double v, String m, String p, AgentId a) {
        tender = t;
        vl = v;
        members = m;
        props =p;
        agid = a;
    }

    public String getAsPrologStr() {
        return "bid("+vl+","+tender+",\""+members+"\",\""+props+"\")"; // bid(bob,400,"me and my dad","in 5 days")
    }

    @Override
    public String toString() {
        return getAsPrologStr();
    }
}
