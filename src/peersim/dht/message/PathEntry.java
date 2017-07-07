package peersim.dht.message;

import org.json.simple.JSONObject;
import peersim.core.Node;
import peersim.dht.DHTProtocol;

/**
 * Created by baumeist on 5/21/17.
 */
public class PathEntry {
    public Node node;
    public DHTProtocol dht;
    public DHTMessage.MessageStatus status;

    public PathEntry(Node n, DHTProtocol dhtNode, DHTMessage.MessageStatus s){
        this.node = n;
        this.dht = dhtNode;
        this.status = s;
    }

    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append(this.node.getID());
        b.append('(').append(lookupStatus(this.status)).append(')');
        return b.toString();
    }

    public JSONObject toJSON(){
        JSONObject entry = new JSONObject();
        entry.put("id", this.node.getID());
        entry.put("address", this.dht.getAddress());
        entry.put("status", this.status.toString());
        return entry;
    }

    private String lookupStatus(DHTMessage.MessageStatus status){
        return status.toString().substring(0,2);
    }
}
