package peersim.dht.message;

import java.util.LinkedList;
import java.util.List;

import peersim.core.Node;
import peersim.dht.loopDetection.LoopDetection;

public class DHTMessage {

	/**
	 * Stores the last used message ID. It is incremented every time a new
	 * message is created.
	 */
	private static long MESSAGE_COUNTER = 0;
	private static Object MESSAGE_LOCK = new Object();

	private int hopCounter = 0;
	private boolean delivered = false;
	private final long messageID;
	private final double targetLocation;
	private DHTPath routingPath = new DHTPath();
	private DHTPath connectionPath = new DHTPath();

	/**
	 * This is the loop detection protocol used by this message.
	 */
	protected LoopDetection loopDetection = null;

	public DHTMessage(LoopDetection loopDetection, double target) {
		// Make setting the message ID thread safe.
		synchronized (DHTMessage.MESSAGE_LOCK) {
			DHTMessage.MESSAGE_COUNTER++;
			this.messageID = DHTMessage.MESSAGE_COUNTER;
		}

		this.loopDetection = loopDetection;
		this.targetLocation = target;
	}
	
	/**
	 * @return the target
	 */
	public double getTarget() {
		return this.targetLocation;
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
	 *         to destination.
	 */
	public DHTPath getConnectionPath() {
		return this.connectionPath;
	}

	/**
	 * Determine if a message can be sent to a node. Typically this fails
	 * because of the loop detection mechanism.
	 * 
	 * @param node
	 *            The next node to rout the message to.
	 * @return True if it is OK to route to the node and False if it is not.
	 */
	public boolean canSend(Node node) {
		return this.loopDetection.canRouteNode(node);
	}

	/**
	 * Called to notify a message that it has been routed to a node.
	 * 
	 * @param node
	 *            Node the received the message.
	 * @return False if the node can't process the message and True if its OK
	 *         for the node to process the message.
	 */
	public boolean received(Node node) {
		this.routingPath.add(node);
		this.connectionPath.add(node);
		return this.loopDetection.addVistedNode(node);
	}

	/**
	 * The message back tracked during routing. This updates the routing paths
	 * stored by the message.
	 * 
	 * @return True if backtracking was successful and False if there is no
	 *         place to backtrack to (e.g. at the source node).
	 */
	public boolean backtrack() {
		if (this.connectionPath.getPathLength() < 1)
			return false;
		// remove the last two nodes added
		this.connectionPath.removeLast();
		this.connectionPath.removeLast();
		return true;
	}

	/**
	 * Increase the hop counter for the message.
	 */
	public void increaseHopCounter() {
		hopCounter++;
	}

	/**
	 * @return the hopCounter
	 */
	public int getHopCounter() {
		return hopCounter;
	}
}
