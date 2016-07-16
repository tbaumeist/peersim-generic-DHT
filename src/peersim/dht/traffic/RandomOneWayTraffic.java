package peersim.dht.traffic;

import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dht.DHTProtocol;
import peersim.dht.message.OneWayPingMessage;
import peersim.dht.utils.DHTControl;
import peersim.edsim.EDSimulator;

/**
 * Generates one way traffic between random nodes.
 * 
 * @author todd
 * 
 */
public class RandomOneWayTraffic extends DHTControl {

	/**
	 * Generate messages that route from a random node A to a random node B
	 * 
	 * @param prefix
	 */
	public RandomOneWayTraffic(String prefix) {
		super(prefix);
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
		OneWayPingMessage message = new OneWayPingMessage(targetProtocol.getAddress());
		this.getDataStore().storeMessage(message);
		EDSimulator.add(10, message, sender, pid);
		return false;
	}
}