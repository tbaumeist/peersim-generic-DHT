package peersim.dht.message;

import peersim.core.Node;
import peersim.dht.utils.Address;

/**
 * Sends a 
 * @author todd
 *
 */
public class CircuitPongMessage extends DHTMessage {

	public CircuitPongMessage(DHTPath routePath, Address targetAddress) {
		super(targetAddress);
		Node next = null;
		for(Node n : routePath){
			if(next != null)
				this.saveRoutingState(n, next);
			next = n;
		}
	}

	@Override
	public DHTMessageAction onDelivered(int pid) {
		return null;
	}

	@Override
	public DHTMessageAction onFailure(int pid) {
		return null;
	}
}

