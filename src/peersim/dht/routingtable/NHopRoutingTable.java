package peersim.dht.routingtable;

import peersim.config.Configuration;
import peersim.core.Linkable;
import peersim.core.Node;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by baumeist on 5/13/17.
 */
public class NHopRoutingTable extends DHTRoutingTable {

    /**
     * The {@value #PAR_HOPS} configuration parameter defines the
     * number of hops the routing table table will use for routing decisions.
     * Defaults to 2 hops.
     *
     * @config
     */
    private static final String PAR_HOPS = "hops";
    private final int hopCount;

    public NHopRoutingTable(String prefix) {
        super(prefix);
        this.hopCount = Configuration.getInt(prefix + "." + PAR_HOPS, 2);
        if(this.hopCount < 1){
            System.err.println("Must have a hop count of at least one for the N Lookup Table");
            System.exit(5); // abort
        }
    }

    public List<RoutingTableEntry> getRoutingTableEntries(Node node, int linkPid){
        List<RoutingTableEntry> entries = new LinkedList<>();
        this.addRoutingEntries(entries, node, linkPid, node, null, 1, this.hopCount);
        return entries;
    }

    public Object clone(){
        return new NHopRoutingTable(this.prefix);
    }

    private void addRoutingEntries(List<RoutingTableEntry> entries, Node currentNode, int linkPid,
                                   Node sourceNode, Node routeToNode,
                                   int currentHop, final int stopHop){
        // stop!
        if( currentHop > stopHop )
            return;

        // get the direct neighbor links
        Linkable linkable = (Linkable) currentNode.getProtocol(linkPid);
        for (int i = 0; i < linkable.degree(); i++) {
            Node n = linkable.getNeighbor(i);

            // don't add your self to the list
            if(n.equals(sourceNode))
                continue;

            Node r = routeToNode;
            if( r == null)
                r = n;
            if(this.addUniqueEntry(entries, r, n, currentHop)){
                // added a new entry, depth first continue adding entries
                this.addRoutingEntries(entries, n, linkPid, sourceNode, r, currentHop+1, stopHop);
            }
        }
    }

    private boolean addUniqueEntry(List<RoutingTableEntry> entries, Node routeToNode, Node targetNode, int hopDistance){
        for(int i =0; i < entries.size(); i++){
            RoutingTableEntry e = entries.get(i);
            // check if there is already an entry for target node through the same route to node
            if( e.targetNode.equals(targetNode) && e.routeToNode.equals(routeToNode)){
                // replace entry if new entry is closer
                if(e.hopDistance > hopDistance){
                    entries.remove(i);
                    entries.add(new RoutingTableEntry(routeToNode, targetNode, hopDistance));
                    return true;
                } else {
                    // Already an equal or better routing entry
                    return false;
                }
            }
        }

        // no equivalent routing entry found, add one
        entries.add(new RoutingTableEntry(routeToNode, targetNode, hopDistance));
        return true;
    }
}
