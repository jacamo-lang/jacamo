/** agent Carlos */

package jadeag;

import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.*;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;

public class Carlos extends Agent {

    @Override
    protected void setup() {
        System.out.println("Starting "+getLocalName());
        addBehaviour(new Hello());
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    System.out.println(msg.getSender().getName() + ": "
                               + msg.getContent());
                } else {
                    block();
                }
           }
       });
    }

    class Hello extends TickerBehaviour {

        Hello() {
            super(Carlos.this, 2000);
        }

        @Override
        public void onTick() {
            try {
                System.out.println("Sending message to Alice");
                ACLMessage m = new ACLMessage(ACLMessage.INFORM);
                m.addReceiver(new AID("alice", AID.ISLOCALNAME));
                m.setContent("hello");
                send(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
