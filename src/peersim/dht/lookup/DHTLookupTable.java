package peersim.dht.lookup;

import peersim.core.Linkable;
import peersim.core.Node;
import peersim.core.Protocol;
import peersim.dht.utils.Address;

import java.util.List;

/**
 * Created by baumeist on 5/13/17.
 */
public abstract class DHTLookupTable implements Protocol {

    protected final String prefix;

    public DHTLookupTable(String prefix) {
        this.prefix = prefix;
    }

    public boolean isDirectNeighbor(Linkable linkable, Node neighbor){
        for (int i = 0; i < linkable.degree(); i++) {
            Node n = linkable.getNeighbor(i);
            if (n.equals(neighbor))
                return true;
        }
        return false;
    }

    public abstract List<LookupEntry> getLookupEntries(Node node, int linkPid);

    public abstract Object clone();


    public class LookupEntry{

        public final Node routeToNode, targetNode;
        public final int hopDistance;

        public LookupEntry(Node routeTo, Node targetNode, int hopDistance){
            this.routeToNode = routeTo;
            this.targetNode = targetNode;
            this.hopDistance = hopDistance;
        }
    }
}
