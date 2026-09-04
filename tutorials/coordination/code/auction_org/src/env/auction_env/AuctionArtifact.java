package auction_env;

import jason.asSyntax.Atom;
import cartago.*;

public class AuctionArtifact extends Artifact {

    String currentWinner = "no_winner";

    public void init()  {
        // observable properties
        defineObsProperty("running",     "no");
        defineObsProperty("best_bid",    Double.MAX_VALUE);
        defineObsProperty("winner",      new Atom(currentWinner)); // Atom is a Jason type
    }

    @OPERATION public void start(String task)  {
        if (getObsProperty("running").stringValue().equals("yes"))
            failed("The protocol is already running and so you cannot start it!");

        defineObsProperty("task", task);
        getObsProperty("running").updateValue("yes");
    }

    @OPERATION public void stop()  {
        if (! getObsProperty("running").stringValue().equals("yes"))
            failed("The protocol is not running, why to stop it?!");

        getObsProperty("running").updateValue("no");
        getObsProperty("winner").updateValue(new Atom(currentWinner));
    }

    @OPERATION public void bid(double bidValue) {
        if (getObsProperty("running").stringValue().equals("no"))
            failed("You can not bid for this auction, it is not running!");

        ObsProperty opCurrentValue  = getObsProperty("best_bid");
        if (bidValue < opCurrentValue.doubleValue()) {  // the bid is better than the previous
            opCurrentValue.updateValue(bidValue);
            currentWinner = getCurrentOpAgentId().getAgentName(); // the name of the agent doing this operation
        }
        System.out.println("Received bid "+bidValue+" from "+getCurrentOpAgentId().getAgentName()+" for "+getObsProperty("task").stringValue());
    }
}
