package peersim.dht.routingtable;

import peersim.core.Linkable;
import peersim.core.Node;
import peersim.core.Protocol;

import java.util.List;

/**
 * Created by baumeist on 5/13/17.
 */
public abstract class DHTRoutingTable implements Protocol {

    protected final String prefix;

    public DHTRoutingTable(String prefix) {
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

    public abstract List<RoutingTableEntry> getRoutingTableEntries(Node node, int linkPid);

    public abstract Object clone();


    public class RoutingTableEntry {

        public final Node routeToNode, targetNode;
        public final int hopDistance;

        public RoutingTableEntry(Node routeTo, Node targetNode, int hopDistance){
            this.routeToNode = routeTo;
            this.targetNode = targetNode;
            this.hopDistance = hopDistance;
        }

        @Override
        public String toString(){
            return String.format("(%d) %d -> %d", this.hopDistance, this.routeToNode.getID(), this.targetNode.getID());
        }
    }
}
