package peersim.dht.utils;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.dht.observer.store.DHTDataStore;
import peersim.dht.observer.store.MemoryStore;

/**
 * Base class for all DHT protocol controls.
 * @author todd
 *
 */
public abstract class DHTControl implements Control {
	
	/**
	 * The protocol to execute the control on.
	 * @config
	 */
	/**
	 * The {@value #PAR_PROT} configuration parameter which references the DHTProtocol object.
	 *
	 * @config
	 */
	protected static final String PAR_PROT = "protocol";

	/**
	 * The {@value #PAR_STORE} configuration parameter defines where the control will store any persistent data
	 * used by observers: defaults to the {@link peersim.dht.observer.store.MemoryStore}
	 * class.
	 *
	 * @config
	 */
	private static final String PAR_STORE = "datastore";
	
	/**
	 * Stores the protocol identifier.
	 */
	protected final int pid;
	
	/**
	 * Stores the control's name.
	 */
	protected final String prefix;

	/**
	 * Used to store persistent data used by observers
	 */
	private DHTDataStore dataStore = null;
	
	/**
	 * Standard constructor.
	 * @param prefix
	 * 			Configuration file prefix.
	 */
	public DHTControl(String prefix){
		this.prefix = prefix;
		this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	/**
	 * Get the persistent data storage object for storing data observers use.
	 * @return DHT Data Store object.
     */
	protected DHTDataStore getDataStore(){
		if (this.dataStore != null)
			return this.dataStore;

		try {
			// Load the configured routing protocol
			this.dataStore =  (DHTDataStore) Configuration.getInstance(this.prefix + "."
					+ PAR_STORE, new MemoryStore(""));
		} catch (Exception e) {
			System.err.println(String.format(
					"Error loading a storage protocol: %s", e.getMessage()));
			System.exit(5); // abort
		}
		return this.dataStore;
	}
}
