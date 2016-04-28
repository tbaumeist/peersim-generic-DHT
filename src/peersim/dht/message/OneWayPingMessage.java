package peersim.dht.message;

import peersim.dht.loopDetection.LoopDetection;

/**
 * Sends a 
 * @author todd
 *
 */
public class OneWayPingMessage extends DHTMessage {


	public OneWayPingMessage(LoopDetection loopDetection, double targetLocation) {
		super(loopDetection, targetLocation);
	}
}

