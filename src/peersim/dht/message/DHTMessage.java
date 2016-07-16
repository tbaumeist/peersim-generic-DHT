package peersim.dht.message;

import peersim.core.Node;
import peersim.dht.utils.Address;

public class DHTMessage {

    /**
     * Stores the last used message ID. It is incremented every time a new
     * message is created.
     */
    private static long MESSAGE_COUNTER = 0;
    private static Object MESSAGE_LOCK = new Object();

    private boolean delivered = false;
    private final long messageID;
    private final Address targetAddress;
    private DHTPath routingPath = new DHTPath();
    private DHTPath connectionPath = new DHTPath();

    public DHTMessage(Address target) {
        // Make setting the message ID thread safe.
        synchronized (DHTMessage.MESSAGE_LOCK) {
            DHTMessage.MESSAGE_COUNTER++;
            this.messageID = DHTMessage.MESSAGE_COUNTER;
        }

        this.targetAddress = target;
    }

    /**
     * @return the target
     */
    public Address getTarget() {
        return this.targetAddress;
    }

    /**
     * @return ID of the message.
     */
    public long getMessageID() {
        return this.messageID;
    }

    /**
     * Called when a message successfully reaches its destination node.
     */
    public void setDelivered() {
        this.delivered = true;
    }

    /**
     * @return True is message reached its destination, otherwise False.
     */
    public boolean getDelivered() {
        return this.delivered;
    }

    /**
     * @return Get the source Node a message was sent from.
     */
    public Node getSourceNode() {
        return this.connectionPath.getSource();
    }

    /**
     * @return Get the destination Node of the message.
     */
    public Node getDestinationNode() {
        return this.connectionPath.getDestination();
    }

    /**
     * @return Get the previous Node a message was routed to.
     */
    public Node getPreviousNode() {
        return this.routingPath.getPreviousNode();
    }

    /**
     * @return The list of nodes the message has been routed through.
     */
    public DHTPath getRoutingPath() {
        return this.routingPath;
    }

    /**
     * @return The list of nodes the make a routing connection from the source
     * to destination.
     */
    public DHTPath getConnectionPath() {
        return this.connectionPath;
    }

    /**
     * Called to notify a message that it has been routed to a node.
     *
     * @param node Node the arrivedAt the message.
     * @return False if the node can't process the message and True if its OK
     * for the node to process the message.
     */
    public void arrivedAt(Node node) {
        this.routingPath.add(node);
        this.connectionPath.add(node);
    }

    /**
     * The message back tracked during routing. This updates the routing paths
     * stored by the message.
     *
     * @return True if backtracking was successful and False if there is no
     * place to backtrack to (e.g. at the source node).
     */
    public boolean backtrack() {
        if (this.connectionPath.getPathLength() < 1)
            return false;
        // remove the last two nodes added
        this.connectionPath.removeLast();
        this.connectionPath.removeLast();
        return true;
    }
}
