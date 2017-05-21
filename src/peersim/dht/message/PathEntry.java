package peersim.dht.message;

import peersim.core.Node;

/**
 * Created by baumeist on 5/21/17.
 */
public class PathEntry {
    public Node node;
    public DHTMessage.MessageStatus status;

    public PathEntry(Node n, DHTMessage.MessageStatus s){
        this.node = n;
        this.status = s;
    }
}
