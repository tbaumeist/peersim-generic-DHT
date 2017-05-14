package peersim.dht;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Node;
import peersim.dht.lookup.DHTLookupTable;
import peersim.dht.lookup.NHopLookupTable;
import peersim.dht.router.DHTRouter;
import peersim.dht.router.DHTRouterGreedy;
import peersim.dht.utils.Address;
import peersim.edsim.EDProtocol;

public class DHTProtocol implements EDProtocol, Cloneable {

    /**
     * The {@value #PAR_LINK} configuration parameter defines which protocol
     * is used to store the links (ie the peer relationships).
     *
     * @config
     */
    private static final String PAR_LINK = "topology";

    /**
     * The {@value #PAR_TRANS} configuration parameter defines which protocol
     * is used to transport the messages (ie the connections between peers).
     *
     * @config
     */
    private static final String PAR_TRANS = "transport";

    /**
     * The {@value #PAR_ROUTER} configuration parameter defines which routing protocol
     * the simulator should use: defaults to the {@link peersim.dht.router.DHTRouterGreedy}
     * class.
     *
     * @config
     */
    private static final String PAR_ROUTER = "router";

    /**
     * The {@value #PAR_LOOKUP_TABLE} configuration parameter defines which lookup table protocol
     * the simulator should use: defaults to the {@link peersim.dht.lookup.NHopLookupTable}
     * class.
     *
     * @config
     */
    private static final String PAR_LOOKUP_TABLE = "lookup_table";

    private final String prefix;
    private final int linkPid, transportPid;
    private DHTRouter router = null;
    private DHTLookupTable lookupTable = null;

    private Address address = null;

    public DHTProtocol(String prefix) {
        this.prefix = prefix;
        this.linkPid = Configuration.getPid(prefix + "." + PAR_LINK);
        this.transportPid = Configuration.getPid(prefix + "." + PAR_TRANS);
        this.address = new Address(CommonState.r.nextDouble());
    }

    @Override
    public void processEvent(Node node, int pid, Object event) {
        this.getRouter(node).route(this.getLookupTable(node), node, pid, this.transportPid, this.linkPid, event);
    }

    /**
     * Set the address of a node in the DHT.
     *
     * @param address value between 0 and 1
     */
    public void setAddress(double address) {

        this.address = new Address(address);
    }

    /**
     * @return address of the node
     */
    public Address getAddress() {
        return this.address;
    }


    private DHTRouter getRouter(Node node){
        if (this.router != null)
            return this.router;
        try {
            // Load the configured routing protocol
            if(Configuration.contains(this.prefix + "." + PAR_ROUTER))
                this.router = (DHTRouter)node.getProtocol(Configuration.getPid(prefix + "." + PAR_ROUTER));
            else
                this.router = new DHTRouterGreedy("");
        } catch (Exception e) {
            System.err.println(String.format("Error loading a routing protocol: %s", e.getMessage()));
            System.exit(5); // abort
        }
        return this.router;
    }

    private DHTLookupTable getLookupTable(Node node){
        if (this.lookupTable != null)
            return this.lookupTable;
        try {
            // Load the configured routing protocol
            if(Configuration.contains(this.prefix + "." + PAR_LOOKUP_TABLE))
                this.lookupTable = (DHTLookupTable) node.getProtocol(Configuration.getPid(prefix + "." + PAR_LOOKUP_TABLE));
            else
                this.lookupTable = new NHopLookupTable("");
        } catch (Exception e) {
            System.err.println(String.format("Error loading a lookup protocol: %s", e.getMessage()));
            System.exit(5); // abort
        }
        return this.lookupTable;
    }

    /**
     * Replicate this object by returning an identical copy.<br>
     * It is called by the initializer and do not fill any particular field.
     *
     * @return Object
     */
    public Object clone() {
        DHTProtocol dolly = new DHTProtocol(this.prefix);
        return dolly;
    }

    @Override
    public String toString() {
        return String.format("DHT Location: %s", this.getAddress());
    }

}
