package peersim.dht;

import peersim.config.Configuration;
import peersim.core.Node;
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
    private static final String PAR_LINK = "linkable";

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

    private final String prefix;
    private final int linkPid, transportPid;

    private Address address = null;
    private DHTRouter router = null;

    public DHTProtocol(String prefix) {
        this.prefix = prefix;
        this.linkPid = Configuration.getPid(prefix + "." + PAR_LINK);
        this.transportPid = Configuration.getPid(prefix + "." + PAR_TRANS);
    }

    @Override
    public void processEvent(Node node, int pid, Object event) {
        this.getRouter().route(node, pid, this.transportPid, this.linkPid, event);
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

    private DHTRouter getRouter(){
        if (this.router != null)
            return this.router;

        try {
            // Load the configured routing protocol
            this.router =  (DHTRouter) Configuration.getInstance(this.prefix + "."
                    + PAR_ROUTER, new DHTRouterGreedy(""));
        } catch (Exception e) {
            System.err.println(String.format(
                    "Error loading a routing protocol: %s", e.getMessage()));
            System.exit(5); // abort
        }
        return this.router;
    }
}
