package peersim.dht.lookup;

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
     * number of hops the lookup table will use for routing decisions.
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

    public List<LookupEntry> getLookupEntries(Node node, int linkPid){
        List<LookupEntry> entries = new LinkedList<>();
        this.addLookupEntries(entries, node, linkPid, null, 1, this.hopCount);
        return entries;
    }

    public Object clone(){
        return new NHopRoutingTable(this.prefix);
    }

    private void addLookupEntries(List<LookupEntry> entries, Node node, int linkPid, Node routeToNode,
                                  int currentHop, final int stopHop){
        // stop!
        if( currentHop > stopHop )
            return;

        // get the direct neighbor links
        Linkable linkable = (Linkable) node.getProtocol(linkPid);
        for (int i = 0; i < linkable.degree(); i++) {
            Node n = linkable.getNeighbor(i);
            Node r = routeToNode;
            if( r == null)
                r = n;
            if(this.addUniqueEntry(entries, r, n, currentHop)){
                // added a new entry, depth first continue adding entries
                this.addLookupEntries(entries, n, linkPid, r, currentHop++, stopHop);
            }
        }
    }

    private boolean addUniqueEntry(List<LookupEntry> entries, Node routeToNode, Node targetNode, int hopDistance){
        for(int i =0; i < entries.size(); i++){
            LookupEntry e = entries.get(i);
            // check if there is already an entry for target node through the same route to node
            if( e.targetNode.equals(targetNode) && e.routeToNode.equals(routeToNode)){
                // replace entry if new entry is closer
                if(e.hopDistance > hopDistance){
                    entries.remove(i);
                    entries.add(new LookupEntry(routeToNode, targetNode, hopDistance));
                    return true;
                } else {
                    // Already an equal or better routing entry
                    return false;
                }
            }
        }

        // no equivalent routing entry found, add one
        entries.add(new LookupEntry(routeToNode, targetNode, hopDistance));
        return true;
    }
}
