package auction_env;

import jason.asSyntax.Atom;
import cartago.*;

public class AuctionArtifact extends Artifact {
	
	String currentWinner = "no_winner";
	
    @OPERATION public void init()  {
        // observable properties   
        defineObsProperty("running",     false);
        defineObsProperty("task",        "no_task");
        defineObsProperty("best_bid",    Double.MAX_VALUE);
        defineObsProperty("winner",      new Atom(currentWinner)); // Atom is a Jason type      
    }

    @OPERATION public void start(String task)  {
    	if (getObsProperty("running").booleanValue())
    		failed("The protocol is already running and so you cannot start it!");
    	
        getObsProperty("running").updateValue(true);
        getObsProperty("task").updateValue(task);
    }
    
    @OPERATION public void stop()  {
    	if (! getObsProperty("running").booleanValue())
    		failed("The protocol is not running, why to stop it?!");
    	
        getObsProperty("running").updateValue(false);
        getObsProperty("winner").updateValue(new Atom(currentWinner));
    }

    @OPERATION public void bid(double bidValue) {
    	if (! getObsProperty("running").booleanValue())
    		failed("You can not bid for this auction, it is not started!");
    	
        ObsProperty opCurrentValue  = getObsProperty("best_bid");
        if (bidValue < opCurrentValue.doubleValue()) {  // the bid is better than the previous
            opCurrentValue.updateValue(bidValue);
            currentWinner = getOpUserName(); // the name of the agent doing this operation
        }
    	System.out.println("Received bid "+bidValue+" from "+getOpUserName()+" for "+getObsProperty("task").stringValue());
    }    
}

