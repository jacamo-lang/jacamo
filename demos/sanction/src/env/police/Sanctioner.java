// CArtAgO artifact code for project sanction

package police;

import cartago.Artifact;
import cartago.ArtifactId;
import cartago.LINK;
import cartago.OPERATION;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.parser.ParseException;

import java.util.logging.Logger;

/**
 * artifact that implements sanctions
 */
public class Sanctioner extends Artifact {
    protected Logger logger = Logger.getLogger(Sanctioner.class.getName());

    // to listen normative events
    @OPERATION
    void listen(ArtifactId artId) {
        try {
            execLinkedOp(artId, "subscribeNormativeEvents", getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @LINK
    void normativeEvent(String event) {
        //logger.info("new normative event: "+event);
        try {
            var evt = ASSyntax.parseLiteral(event);
            if (evt.getFunctor().equals("sanction")) {
                var sanction = (Literal)evt.getTerm(1);
                if (sanction.getFunctor().equals("remove_from_systems")) {
                    logger.info("**** I am implementing the sanction for "+evt.getTerm(0)+" ****");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}

