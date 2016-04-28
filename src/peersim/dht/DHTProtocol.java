package peersim.dht;

import java.util.LinkedList;
import java.util.List;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.dht.loopDetection.GUIDLoopDetection;
import peersim.dht.loopDetection.LoopDetection;
import peersim.dht.message.DHTMessage;
import peersim.dht.message.OneWayPingMessage;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;

public class DHTProtocol implements EDProtocol, Cloneable {

	/**
	 * The {@value #PAR_LOOP} configuration parameter defines which loop
	 * detection protocol message sent by the simulator should use. This
	 * defaults to the {@link peersim.dht.loopDetection.GUIDLoopDetection}
	 * class.
	 * 
	 * @config
	 */
	private static final String PAR_LOOP = "loop";

	private static List<DHTMessage> allMessages = null;
	
	private final String prefix;
	private double location = 0;

	public DHTProtocol(String prefix) {
		this.prefix = prefix;
	}

	public LoopDetection createLoopDetection() {
		try {
			// Load the configured loop detection protocol
			// By default GUID based loop detection is used
			return (LoopDetection) Configuration.getInstance(this.prefix + "."
					+ PAR_LOOP, new GUIDLoopDetection(""));

		} catch (Exception e) {
			System.err.println(String.format(
					"Error loading a loop protocol: %s", e.getMessage()));
			System.exit(5); // abort
		}
		return null;
	}

	private double diff(double a, double b) {
		return Math.abs(a - b);
	}

	@Override
	public void processEvent(Node node, int pid, Object event) {

		OneWayPingMessage m = (OneWayPingMessage) event;
		DHTProtocol currentNode = (DHTProtocol) node.getProtocol(pid);
		Transport transport = (Transport) node.getProtocol(FastConfig
				.getTransport(pid));

		// Add node to the messages visited list
		if (!m.received(node)) {
			// already visited, backtrack to previous node
			Node previous = m.getPreviousNode();
			if (m.backtrack())
				transport.send(node, previous, m, pid);
			return;
		}

		if (currentNode.getLocation() == m.getTarget()) {
			m.setDelivered();
			return; // reached target
		}

		Linkable linkable = (Linkable) node.getProtocol(FastConfig
				.getLinkable(pid));
		Node next = null;
		DHTProtocol nextDHT = null;
		for (int i = 0; i < linkable.degree(); i++) {
			Node n = linkable.getNeighbor(i);
			if (n.equals(node))
				continue;
			DHTProtocol nDHT = (DHTProtocol) n.getProtocol(pid);
			if (next == null
					|| diff(nDHT.getLocation(), m.getTarget()) < diff(
							nextDHT.getLocation(), m.getTarget())) {
				next = n;
				nextDHT = (DHTProtocol) next.getProtocol(pid);
			}
		}

		transport.send(node, next, m, pid);
	}

	/**
	 * Set the location of a node in the DHT.
	 * 
	 * @param location
	 *            value between 0 and 1
	 */
	public void setLocation(double location) {
		this.location = Math.abs(location) % 1.0;
	}

	/**
	 * @return location of the node
	 */
	public double getLocation() {
		return this.location;
	}
	
	/**
	 * Store a given message.
	 * @param message 
	 * 			The message to store.
	 */
	public void storeMessage(DHTMessage message){
		if(DHTProtocol.allMessages == null)
			return;
		
		DHTProtocol.allMessages.add(message);
	}
	
	/**
	 * Called by controls to enable message storage.
	 */
	public void enableMessageStorage() {
		DHTProtocol.allMessages = new LinkedList<DHTMessage>();
	}

	/**
	 * Called by controls to get all stored messages.
	 * @return List of all messages stored. Null if storage not enabled.
	 */
	public List<DHTMessage> getAllMessages() {
		return DHTProtocol.allMessages;
	}

	/**
	 * Clears all stored messages.
	 */
	public void clearMessages() {
		DHTProtocol.allMessages.clear();
	}

	/**
	 * Replicate this object by returning an identical copy.<br>
	 * It is called by the initializer and do not fill any particular field.
	 * 
	 * @return Object
	 */
	public Object clone() {
		DHTProtocol dolly = new DHTProtocol(this.prefix);
		return dolly;
	}

	@Override
	public String toString() {
		return String.format("DHT Location: %f", this.getLocation());
	}

}
