package peersim.dht.network;

import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dht.DHTControl;
import peersim.dht.DHTProtocol;

public class RandomNetwork extends DHTControl {
	
	public RandomNetwork(String prefix) {
		super(prefix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see peersim.core.Control#execute()
	 */

	public boolean execute() {
		for (int i = 0; i < Network.size(); i++) {
			Node node = (Node) Network.get(i);
			DHTProtocol cp = (DHTProtocol) node.getProtocol(pid);
			cp.setLocation(CommonState.r.nextDouble());
		}
		return false;
	}
}
