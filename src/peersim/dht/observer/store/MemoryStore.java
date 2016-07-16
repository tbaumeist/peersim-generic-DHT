package peersim.dht.observer.store;

import peersim.dht.message.DHTMessage;

import java.util.LinkedList;
import java.util.List;

public class MemoryStore extends DHTDataStore {
    private static List<DHTMessage> allMessages = new LinkedList<>();

    public MemoryStore(String prefix){
        super(prefix);
    }

    /**
     * Store a given message.
     *
     * @param message The message to store.
     */
    public void storeMessage(DHTMessage message) {
        if(!this.isEnabled())
            return;
        MemoryStore.allMessages.add(message);
    }

    /**
     * Called by controls to get all stored messages.
     *
     * @return List of all messages stored. Null if storage not enabled.
     */
    public List<DHTMessage> getAllMessages() {
        return MemoryStore.allMessages;
    }

    /**
     * Clears all stored messages.
     */
    public void clearMessages() {
        MemoryStore.allMessages.clear();
    }
}
