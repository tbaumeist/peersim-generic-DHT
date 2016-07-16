package peersim.dht.message;

import peersim.dht.utils.Address;

/**
 * Sends a 
 * @author todd
 *
 */
public class OneWayPingMessage extends DHTMessage {


	public OneWayPingMessage(Address targetAddress) {
		super(targetAddress);
	}
}

