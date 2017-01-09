package peersim.dht.router;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.dht.DHTProtocol;
import peersim.dht.message.DHTMessage;
import peersim.dht.utils.Address;
import peersim.transport.Transport;

import java.util.Collection;
import java.util.LinkedList;

public class DHTRouterGreedy extends DHTRouter {

    /**
     * The {@value #PAR_RANDOMNESS} configuration parameter defines the
     * amount of randomness to introduce into the greedy routing algorithm:
     * defaults to the value 0.0. The value range is 0.0 - 1.0. 0 is no
     * randomness and 1 is completely random.
     *
     * @config
     */
    private static final String PAR_RANDOMNESS = "randomness";

    private final double randomness;

    public DHTRouterGreedy(String prefix) {
        super(prefix);
        this.randomness = Configuration.getDouble(prefix + "." + PAR_RANDOMNESS, 0.0);
    }

    private void sendNext(Node node, int pid, int transportPid, int linkPid, DHTMessage message, Node next,
                          RoutingState state){
        Transport transport = (Transport) node.getProtocol(transportPid);

        if(next == null){
            // ran out of peers to successfully route the message
            this.sendBacktrack(node, pid, transportPid, linkPid, message, state.getPrevious());
        } else {
            state.addRoutedTo(next);
            transport.send(node, next, message, pid);
        }
    }

    @Override
    protected void routeNextNode(Node node, int pid, int transportPid, int linkPid, DHTMessage message){
        Linkable linkable = (Linkable) node.getProtocol(linkPid);

        //  Is this the first time this node has handled this message/
        if(!message.hasRoutingState(node)) {
            // Should we do random routing here?
            boolean routeRandomly = CommonState.r.nextDouble() <= this.randomness;
            // create and save a routing state
            message.saveRoutingState(node, new RoutingState(message.getPreviousNode(), routeRandomly));
        }

        RoutingState state = (RoutingState)message.getRoutingState(node);

        // Two routing modes, random and greedy
        Node next = null;
        if(state.isRandomRouting()) {
            next = this.getRandomNode(linkable, node, state.getPrevious(), state.getRoutedTo());
        } else {
            next = this.getNextGreedyNode(linkable, node, pid, message.getTarget(), state.getPrevious(),
                    state.getRoutedTo());
        }
        this.sendNext(node, pid, transportPid, linkPid, message, next, state);
    }

    public Object clone() {
        DHTRouterGreedy dolly = new DHTRouterGreedy(this.prefix);
        return dolly;
    }

    private Node getRandomNode(Linkable linkable, Node node, Node previous, Collection<Node> alreadyTried)
    {
        LinkedList<Node> candidates = new LinkedList<>();
        // randomly order list of peers
        for (int i = 0; i < linkable.degree(); i++) {
            candidates.add(CommonState.r.nextInt(candidates.size()), linkable.getNeighbor(i));
        }

        while(candidates.size() > 0){
            Node n = candidates.pop();
            if (n.equals(node))
                continue;
            if(n.equals(previous))
                continue;
            if(alreadyTried != null && alreadyTried.contains(n))
                continue;
            return n; // found one
        }
        return null;
    }

    private Node getNextGreedyNode(Linkable linkable, Node node, int pid, Address target, Node previous,
                                   Collection<Node> alreadyTried){
        Node next = null;
        DHTProtocol nextDHT = null;
        for (int i = 0; i < linkable.degree(); i++) {
            Node n = linkable.getNeighbor(i);
            if (n.equals(node))
                continue;
            if(n.equals(previous))
                continue;
            if(alreadyTried != null && alreadyTried.contains(n))
                continue;
            DHTProtocol nDHT = (DHTProtocol) n.getProtocol(pid);
            if (next == null ||
                    nDHT.getAddress().distance(target) <
                            nextDHT.getAddress().distance(target)) {
                next = n;
                nextDHT = (DHTProtocol) next.getProtocol(pid);
            }
        }
        return next;
    }

    private class RoutingState{
        private final Node previous;
        private LinkedList<Node> routedTo = new LinkedList<>();
        private boolean isRandom = false;

        public RoutingState(Node previous, boolean randomRouting){
            this.previous = previous;
            this.isRandom = randomRouting;
        }

        public Node getPrevious(){ return this.previous; }

        public boolean isRandomRouting(){ return this.isRandom; }

        public void addRoutedTo(Node n){
            this.routedTo.add(n);
        }

        public Collection<Node> getRoutedTo(){ return this.routedTo; }
    }
}
