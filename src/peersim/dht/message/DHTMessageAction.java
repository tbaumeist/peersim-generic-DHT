package peersim.dht.message;

import peersim.core.Node;

/**
 * Created by baumeist on 1/8/17.
 */
public class DHTMessageAction {
    private final Node nextNode;
    private final DHTMessage message;

    public DHTMessageAction(Node next, DHTMessage message){
        this.nextNode = next;
        this.message = message;
    }

    public Node getNextNode(){ return this.nextNode;}

    public DHTMessage getMessage(){ return this.message;}
}
