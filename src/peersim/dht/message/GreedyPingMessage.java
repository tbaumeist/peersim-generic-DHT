package peersim.dht.message;

import peersim.dht.DHTProtocol;
import peersim.dht.utils.Address;

/**
 * Sends a 
 * @author todd
 *
 */
public class GreedyPingMessage extends DHTMessage {


	public GreedyPingMessage(Address targetAddress) {
		super(targetAddress);
	}

	@Override
	public DHTMessageAction onDelivered(int pid) {
		DHTPath path = this.getConnectionPath();
		DHTProtocol targetProtocol = (DHTProtocol) path.getSource().getProtocol(pid);

		return new DHTMessageAction(this.getDestinationNode(),
				new CircuitPongMessage(path, targetProtocol.getAddress()));
	}

	@Override
	public DHTMessageAction onFailure(int pid) {
		return null;
	}
}

