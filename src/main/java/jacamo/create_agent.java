package jacamo;

import java.util.List;

import jaca.CAgentArch;
import jacamo.infra.JaCaMoAgArch;
import jason.asSemantics.TransitionSystem;
import jason.asSyntax.Term;
import jason.runtime.Settings;

/* 
 * internal action to create agents for jacamo
 * it adds the cartago arch.
 */
public class create_agent extends jason.stdlib.create_agent {

    @Override
    protected List<String> getAgArchClasses(Term[] args) {
        List<String> r = super.getAgArchClasses(args);
        if (r.isEmpty()) { // the user has not set ag archs
            r.add(CAgentArch.class.getName());
            r.add(JaCaMoAgArch.class.getName());
        }
        return r;
    }
    
    @Override
    protected Settings getSettings(TransitionSystem ts) {
        return new Settings();
    }
}
