package peersim.dht.observer.store;

import peersim.dht.message.DHTMessage;
import java.util.List;

public abstract class DHTDataStore {

    private static boolean enabled = false;

    private final String prefix;

    public DHTDataStore(String prefix){
        this.prefix = prefix;
    }

    /**
     * Store a given message.
     *
     * @param message The message to store.
     */
    public abstract void storeMessage(DHTMessage message);

    /**
     * Called by controls to get all stored messages.
     *
     * @return List of all messages stored. Null if storage not enabled.
     */
    public abstract List<DHTMessage> getAllMessages();

    /**
     * Clears all stored messages.
     */
    public abstract void clearMessages();

    /**
     * Turn on the data storage.
     */
    public void enable(){
        DHTDataStore.enabled = true;
    }

    /**
     *
     * @return True if the data store has been enabled.
     */
    public boolean isEnabled(){
        return DHTDataStore.enabled;
    }
}
