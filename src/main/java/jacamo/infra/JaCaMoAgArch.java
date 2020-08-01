package jacamo.infra;

import java.util.logging.Level;

import jaca.CAgentArch;
import jacamo.project.JaCaMoAgentParameters;
import jacamo.project.JaCaMoWorkspaceParameters;
import jason.architecture.AgArch;
import jason.asSemantics.Intention;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.runtime.Settings;

/**
 * This class provides an agent architecture when using JaCaMo
 * infrastructure to run the MAS inside Jason.
 *
 * The communication and agent management comes from Centralised Infra and
 * Perceive and Act are delegated to Cartago.
 *
 */
public class JaCaMoAgArch extends AgArch {
    private static final long serialVersionUID = 1L;
    
    public static Atom jcmAtom = new Atom("jcm");

    @Override
    public void init() throws Exception {
        JaCaMoAgentParameters ap = null;
        try {
            ap = (JaCaMoAgentParameters)getTS().getSettings().getUserParameters().get(Settings.PROJECT_PARAMETER);
        } catch (Exception e) {
            getTS().getLogger().warning("error getting parameters to init JaCaMoAgArch! "+e);
            return;
        }

        if (ap == null)
            return;

        getTS().getLogger().fine("Using parameters from project "+ap.getProject().getSocName()+" for agent "+ap.getAgName());

        ListTerm lart = new ListTermImpl();  // list used to produce a goal to join/focus on artifacts
        ListTerm tail = lart;

        // get my WSPs from AgentParameters
        for (String wId: ap.getWorkspaces()) {
            try {
                JaCaMoWorkspaceParameters w = ap.getProject().getWorkspace(wId);
                if (w == null) {
                    getTS().getLogger().warning("**** Workspace "+wId+" is not defined! The agent will not join it.");
                    continue;
                }
                Literal art = ASSyntax.createLiteral("art_env",
                        ASSyntax.createAtom(w.getName()), // workspace
                        ASSyntax.createString(""), // art
                        Literal.DefaultNS);           // namespace

                if (!lart.contains(art))
                    tail = tail.append(art);

            } catch (Exception e) {
                getTS().getLogger().log(Level.SEVERE,"error joining workspace "+wId,e);
            }
        }

        // focus on artifacts
        for (String[] f: ap.getFocus()) {
            Literal art = ASSyntax.createLiteral("art_env",
                    ASSyntax.createAtom(f[1]),  // workspace
                    ASSyntax.createAtom(f[0]), // art
                    ASSyntax.parseTerm(f[2])); // namespace
            if (!lart.contains(art))
                tail = tail.append(art);
        }

        // focus on group artifacts and adopt roles
        ListTerm lroles = new ListTermImpl();
        tail = lroles;
        for (String[] r: ap.getRoles()) {
            if (r[0] == null) {
                getTS().getLogger().warning("No organisation for group "+r[1]+"! Ignoring role "+r[2]);
                continue;
            }
            Literal role = ASSyntax.createLiteral("role",
                    ASSyntax.createAtom(r[0]),   // org
                    ASSyntax.createAtom(r[1]),   // art
                    ASSyntax.createAtom(r[2]));  // role
            if (!lroles.contains(role)) {
                tail = tail.append(role);

                // add auto focus for this group
                Literal art = ASSyntax.createLiteral("art_env",
                        ASSyntax.createAtom(r[0]),    // workspace
                        ASSyntax.createAtom(r[1]),    // art
                        Literal.DefaultNS);           // namespace
                if (!lart.contains(art))
                    lart.append(art);

                // add auto focus on org board
                art = ASSyntax.createLiteral("art_env",
                        ASSyntax.createAtom(r[0]),    // workspace
                        ASSyntax.createAtom(r[0]),    // art
                        Literal.DefaultNS);           // namespace
                if (!lart.contains(art))
                    lart.append(art);
            }
        }

        if (! lart.isEmpty()) {
            if (getTS().getLogger().isLoggable(Level.FINE)) getTS().getLogger().fine("producing goal to focus on "+lart);
            Intention i = new Intention();
            i.setAtomic(1); // force this event to be selected first
            getTS().getC().addAchvGoal( ASSyntax.createLiteral(jcmAtom, "focus_env_art", lart, ASSyntax.createNumber(5)), i);
        }

        if (! lroles.isEmpty()) {
            if (getTS().getLogger().isLoggable(Level.FINE)) getTS().getLogger().fine("producing goal for initial roles "+lroles);
            getTS().getC().addAchvGoal( ASSyntax.createLiteral(jcmAtom, "initial_roles", lroles, ASSyntax.createNumber(5)), Intention.EmptyInt);
        }
    }

    protected CAgentArch getCartagoArch() {
        AgArch arch = getTS().getAgArch().getFirstAgArch();
        while (arch != null) {
            if (arch instanceof CAgentArch) {
                return (CAgentArch)arch;
            }
            arch = arch.getNextAgArch();
        }
        return null;
    }
}
