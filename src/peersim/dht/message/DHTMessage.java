package peersim.dht.message;

import peersim.core.Node;
import peersim.dht.utils.Address;

import java.util.Hashtable;
import java.util.List;

/**
 * Used to represent a message being sent. The message class also contains all "state" information for nodes.
 * This is done to reduce the data stored at each node and having to deal with purging the data when it is no
 * longer needed.
 */
public abstract class DHTMessage {

    /**
     * Stores the last used message ID. It is incremented every time a new
     * message is created.
     */
    private static long MESSAGE_COUNTER = 0;
    private static Object MESSAGE_LOCK = new Object();

    protected final long messageID;
    protected final Address targetAddress;
    protected DHTPath routingPath = new DHTPath();
    protected DHTPath connectionPath = new DHTPath();
    protected Hashtable<Node, Object> routingState = new Hashtable<>();
    protected MessageStatus messageStatus = MessageStatus.UNKNOWN;

    public enum MessageStatus{
        FORWARDED,
        BACKTRACKED,
        FAILED,
        DELIVERED,
        UNKNOWN
    }

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

    public MessageStatus getMessageStatus(){ return this.messageStatus;}

    public void setMessageStatus(MessageStatus status){ this.messageStatus = status;}

    /**
     * @return ID of the message.
     */
    public long getMessageID() {
        return this.messageID;
    }

    /**
     * @return The next action or message to send. Return null if there is no further action.
     */
    public abstract DHTMessageAction onDelivered(int pid);

    /**
     * @return The next action or message to send. Return null if there is no further action.
     */
    public abstract DHTMessageAction onFailure(int pid);

    /**
     * @return True is message reached its destination, otherwise False.
     */
    public boolean wasDelivered() {
        return this.messageStatus == MessageStatus.DELIVERED;
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
     */
    public void arrivedAt(Node node) {
        this.routingPath.add(node);
        // Don't add to connection path if it is already the last node (backtracking)
        if(this.connectionPath.isEmpty() || !node.equals(this.connectionPath.getLast()))
            this.connectionPath.add(node);
    }

    /**
     * The message back tracked during routing. This updates the routing paths
     * stored by the message.
     *
     * @return True if backtracking was successful and False if there is no
     * place to backtrack to (e.g. at the source node).
     */
    public boolean prepareToBacktrack() {
        if (this.connectionPath.getPathLength() <= 0)
            return false;
        // remove the last node added
        this.connectionPath.removeLast();
        return true;
    }

    public String buildPathString(List<Node> path) {
        StringBuilder b = new StringBuilder();
        for (Node n : path) {
            b.append(n.getID());
            b.append(">");
        }
        if (b.length() < 1)
            return "";
        return b.substring(0, b.length() - 1);
    }

    public void saveRoutingState(Node n, Object state){
        this.routingState.put(n, state);
    }

    public boolean hasRoutingState(Node n){ return this.routingState.containsKey(n); }

    public Object getRoutingState(Node n){
        if(this.hasRoutingState(n))
            return this.routingState.get(n);
        return null;
    }
}
