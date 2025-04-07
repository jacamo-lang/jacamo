package jia;

import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.Random;
import java.util.Iterator;

public class random extends DefaultInternalAction {

    private Random random = new Random();

    @Override
    public Object execute(final TransitionSystem ts, final Unifier un, final Term[] args) throws Exception {
        try {
            if (!args[0].isVar()) {
                throw new JasonException("The first argument of the internal action 'random' is not a variable.");
            }
            if (!args[1].isNumeric()) {
                throw new JasonException("The second argument of the internal action 'random' is not a number.");
            }
            final int max = (int)((NumberTerm)args[1]).solve();

            final int maxIter = args.length < 3 ? Integer.MAX_VALUE : (int)((NumberTerm)args[2]).solve();

            return new Iterator<Unifier>() {
                int i = 0;

                // we always have a next random number
                public boolean hasNext() {
                    return i < maxIter && ts.getUserAgArch().isRunning();
                }

                public Unifier next() {
                    i++;
                    Unifier c = un.clone();
                    c.unifies(args[0], new NumberTermImpl(random.nextInt(max)));
                    return c;
                }

                public void remove() {}
            };

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new JasonException("The internal action 'random' has not received the required argument.");
        } catch (Exception e) {
            throw new JasonException("Error in internal action 'random': " + e, e);
        }
    }
}
