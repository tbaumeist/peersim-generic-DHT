package peersim.dht.router;

import peersim.core.Linkable;
import peersim.core.Node;
import peersim.dht.message.DHTMessage;
import peersim.transport.Transport;

public class DHTRouterCircuit extends DHTRouter {

    public DHTRouterCircuit(String prefix) {
        super(prefix);
    }

    @Override
    protected void routeNextNode(Node node, int pid, int transportPid, int linkPid, DHTMessage message) {
        Transport transport = (Transport) node.getProtocol(transportPid);
        Linkable linkable = (Linkable) node.getProtocol(linkPid);

        Node next = (Node)message.getRoutingState(node);

        //check the next node can be routed to
        if(!this.hasNodeNeighbor(linkable, next)){
            message.setMessageStatus(DHTMessage.MessageStatus.FAILED);
            this.storeRouteData(message);
            return; // can't route back to home, because circuit was broken
        }
        transport.send(node, next, message, pid);
    }

    public Object clone() {
        DHTRouterCircuit dolly = new DHTRouterCircuit(this.prefix);
        return dolly;
    }

    private boolean hasNodeNeighbor(Linkable linkable, Node node){
        for (int i = 0; i < linkable.degree(); i++) {
            Node n = linkable.getNeighbor(i);
            if (n.equals(node))
                return true;
        }
        return false;
    }
}
