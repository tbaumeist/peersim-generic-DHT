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

    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append(this.node.getID());
        b.append('(').append(lookupStatus(this.status)).append(')');
        return b.toString();
    }

    private String lookupStatus(DHTMessage.MessageStatus status){
        return status.toString().substring(0,2);
    }
}
