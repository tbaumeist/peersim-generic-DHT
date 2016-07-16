package peersim.dht.router;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.dht.DHTProtocol;
import peersim.dht.message.DHTMessage;
import peersim.transport.Transport;

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

    public void route(Node node, int pid, int transportPid, int linkPid, Object event) {
        DHTMessage message = (DHTMessage) event;
        DHTProtocol currentNode = (DHTProtocol) node.getProtocol(pid);
        Transport transport = (Transport) node.getProtocol(transportPid);
        Linkable linkable = (Linkable) node.getProtocol(linkPid);

        // Add node to the messages visited list
        message.arrivedAt(node);

        // run loop detection
        if (!this.getLoopDetection().addVisitedNode(node)) {
            // already visited
            if (this.canBackTrack) {
                // backtracking
                Node previous = message.getPreviousNode();
                if (message.backtrack())
                    transport.send(node, previous, message, pid);
            }
            return; // either backtracked or we are done
        }

        // final destination, no not the movie
        if (currentNode.getAddress().equals(message.getTarget())) {
            message.setDelivered();
            return; // reached target
        }

        // Route randomly??
        if(CommonState.r.nextDouble() <= this.randomness){
            Node next = this.getRandomNode(linkable, node);
            if(next != null) {
                transport.send(node, next, message, pid);
                return;
            }
        }

        // greedy route to the next closest node
        Node next = this.getNextGreedyNode(linkable, node, pid, message);
        if(next == null)
            return; // no node found to route to TODO: backtrack
        transport.send(node, next, message, pid);
    }

    public Object clone() {
        DHTRouterGreedy dolly = new DHTRouterGreedy(this.prefix);
        return dolly;
    }

    private Node getRandomNode(Linkable linkable, Node node)
    {
        if(linkable.degree() < 2)
            return null;

        Node next = null;
        while(next != null){
            Node n = linkable.getNeighbor(CommonState.r.nextInt(linkable.degree()));
            if (n.equals(node))
                continue;
        }
        return next;
    }

    private Node getNextGreedyNode(Linkable linkable, Node node, int pid, DHTMessage message){
        Node next = null;
        DHTProtocol nextDHT = null;
        for (int i = 0; i < linkable.degree(); i++) {
            Node n = linkable.getNeighbor(i);
            if (n.equals(node))
                continue;
            DHTProtocol nDHT = (DHTProtocol) n.getProtocol(pid);
            if (next == null ||
                    nDHT.getAddress().distance(message.getTarget()) <
                            nextDHT.getAddress().distance(message.getTarget())) {
                next = n;
                nextDHT = (DHTProtocol) next.getProtocol(pid);
            }
        }
        return next;
    }
}
