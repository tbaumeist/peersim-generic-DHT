package peersim.dht.utils;

import peersim.config.Configuration;
import peersim.core.Control;

/**
 * Base class for all DHT protocol controls.
 * @author todd
 *
 */
public abstract class DHTControl implements Control {

	/**
	 * The {@value #PAR_PROT} configuration parameter which references the DHTProtocol object.
	 *
	 * @config
	 */
	protected static final String PAR_PROT = "protocol";
	
	/**
	 * Stores the protocol identifier.
	 */
	protected final int pid;
	
	/**
	 * Stores the control's name.
	 */
	protected final String prefix;
	
	/**
	 * Standard constructor.
	 * @param prefix
	 * 			Configuration file prefix.
	 */
	public DHTControl(String prefix){
		this.prefix = prefix;
		this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
	}


}
