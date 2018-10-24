package jacamo.infra;

import java.util.logging.Level;

import cartago.CartagoException;
import cartago.Op;
import cartago.WorkspaceId;
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

    Atom jcmAtom = new Atom("jcm");

    @Override
    public void init() throws Exception {
        // change the implementation of .create_agent for this agent, use jacamo create agent instead of jason create agent
        getTS().getAg().setIA("jason.stdlib.create_agent", new jacamo.create_agent());

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
                String host = null;
                // it seems that join (remote at least) here is not working....
                // I added a goal for the agent to join (see below)
                //OpFeedbackParam<WorkspaceId> res = new OpFeedbackParam<WorkspaceId>();
                if (w.getNode() != null && !ap.getProject().isInDeployment(w.getNode())) { // there is a node and this node is not deployed locally, then it is a remote workspace
                    host = ap.getProject().getNodeHost(w.getNode());
                    if (host == null) {
                        getTS().getLogger().warning("**** No host is defined for node "+w.getNode()+"! The agent will not join workspace "+w.getName());
                        continue;
                    //} else {
                        //agent.getSession().doAction(new Op("joinRemoteWorkspace", w.getName(), h, res), null, -1);
                    }
                } else {
                    host = "local";
                    //agent.getSession().doAction(new Op("joinWorkspace", w.getName(), res), null, -1);
                }
                Literal art = ASSyntax.createLiteral("art_env",
                        ASSyntax.createAtom(w.getName()), // workspace
                        ASSyntax.createString(host), // host
                        ASSyntax.createString(""), // art
                        Literal.DefaultNS);           // namespace

                if (!lart.contains(art))
                    tail = tail.append(art);


                // wait the join to finish
                /*
                WorkspaceId wspId = res.get();
                int t = 0;
                while (wspId == null && t++ < 10) {
                    Thread.sleep(20*t); // try latter
                    wspId = res.get();
                }
                if (wspId != null) {
                    getTS().getAg().addBel(ASSyntax.createLiteral("jcm__ws",
                            ASSyntax.createString(w.getName()),
                            agent.getJavaLib().objectToTerm(wspId)));
                    getTS().getLogger().info("join "+w.getName()+" ok!");
                } else {
                    getTS().getLogger().warning("join "+w.getName()+"@"+w.getNode()+" ("+h+") failed!");
                }
                */

                /*

                // adopt roles, ....
                for (String[] r: ap.getRoles()) {
                    if (r[0].equals(w.getName())) {
                        getTS().getLogger().fine("adopting role "+r[2]+" in group "+r[1]);
                        ArtifactId aid = JaCaMoLauncher.getJaCaMoRunner().getArtId(r[1]);
                        if (aid == null) {
                            getTS().getLogger().warning("artifac id for "+r[1]+" not found! I cannot adopt role "+r[2]);
                        } else {
                            agent.getSession().doAction(aid, new Op("adoptRole", new Object[] {r[2]} ), null, -1);

                            // focus on this groups (does not work, we used the goal)
                            //agent.getSession().doAction(aid.getWorkspaceId(), new Op("focus", aid), null, -1);
                        }
                    }
                }
                */
            } catch (Exception e) {
                getTS().getLogger().log(Level.SEVERE,"error joining workspace "+wId,e);
            }
        }

        // focus on artifacts
        for (String[] f: ap.getFocus()) {
            String host = ap.getProject().getWorkspaceHost(f[1]);
            if (host == null)
                host = "local"; //InetAddress.getLocalHost().getHostAddress();
            Literal art = ASSyntax.createLiteral("art_env",
                    ASSyntax.createAtom(f[1]),  // workspace
                    ASSyntax.createString(host),  // host
                    ASSyntax.createAtom(f[0]), // art
                    ASSyntax.parseTerm(f[2])); // namespace
            if (!lart.contains(art))
                tail = tail.append(art);
        }

        // focus on group artifacts and adopt roles
        ListTerm lroles = new ListTermImpl();
        tail = lroles;
        String host = null;
        for (String[] r: ap.getRoles()) {
            if (r[0] == null) {
                getTS().getLogger().warning("No organisation for group "+r[1]+"! Ignoring role "+r[2]);
                continue;
            }
            host = ap.getProject().getWorkspaceHost(r[0]);
            if (host == null)
                host = "local";
            Literal role = ASSyntax.createLiteral("role",
                    ASSyntax.createAtom(r[0]),   // org
                    ASSyntax.createString(host), // host
                    ASSyntax.createAtom(r[1]),   // art
                    ASSyntax.createAtom(r[2]));  // role
            if (!lroles.contains(role)) {
                tail = tail.append(role);

                // add auto focus for this group
                Literal art = ASSyntax.createLiteral("art_env",
                        ASSyntax.createAtom(r[0]),    // workspace
                        ASSyntax.createString(host),  // host
                        ASSyntax.createAtom(r[1]),    // art
                        Literal.DefaultNS);           // namespace
                if (!lart.contains(art))
                    lart.append(art);

                // add auto focus on org board
                art = ASSyntax.createLiteral("art_env",
                        ASSyntax.createAtom(r[0]),    // workspace
                        ASSyntax.createString(host),  // host
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

    @Override
    public void stop() {
        super.stop();

        final CAgentArch agent = getCartagoArch();
        for (WorkspaceId wid: agent.getAllJoinedWsps()) {
            try {
                agent.getSession().doAction(wid, new Op("quitWorkspace"), null, -1);
                //getTS().getLogger().info("quit "+wid.getName());
            } catch (CartagoException e) {
            } catch (Exception e) {
                if (! (e instanceof InterruptedException)) {
                    e.printStackTrace();
                }
            }
        }
    }


    protected CAgentArch getCartagoArch() {
        AgArch arch = getTS().getUserAgArch().getFirstAgArch();
        while (arch != null) {
            if (arch instanceof CAgentArch) {
                return (CAgentArch)arch;
            }
            arch = arch.getNextAgArch();
        }
        return null;
    }
}
