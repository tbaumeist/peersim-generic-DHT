package peersim.dht.traffic;

import java.util.LinkedList;
import java.util.List;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dht.DHTProtocol;
import peersim.dht.message.OneWayPingMessage;
import peersim.edsim.EDSimulator;

/**
 * Generates one way traffic between random nodes.
 * 
 * @author todd
 * 
 */
public class RandomOneWayTraffic implements Control {
	/**
	 * The configuration {@value #PAR_PROT} references the DHT protocol.
	 * 
	 * @config
	 */
	private static final String PAR_PROT = "protocol";

	private final int pid;

	/**
	 * Generate messages that route from a random node A to a random node B
	 * 
	 * @param prefix
	 */
	public RandomOneWayTraffic(String prefix) {
		this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	public boolean execute() {
		int size = Network.size();
		Node sender, target;
		do {
			sender = Network.get(CommonState.r.nextInt(size));
			target = Network.get(CommonState.r.nextInt(size));
		} while (sender == null || sender.isUp() == false || target == null
				|| target.isUp() == false);
		DHTProtocol targetProtocol = (DHTProtocol) target.getProtocol(pid);
		OneWayPingMessage message = new OneWayPingMessage(
				targetProtocol.createLoopDetection(),
				targetProtocol.getLocation());
		targetProtocol.storeMessage(message);
		EDSimulator.add(10, message, sender, pid);
		return false;
	}
}