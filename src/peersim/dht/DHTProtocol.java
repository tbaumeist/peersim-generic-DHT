package peersim.dht;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Node;
import peersim.dht.router.DHTRouter;
import peersim.dht.router.DHTRouterCircuit;
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
     * The {@value #PAR_ROUTE_GREEDY} configuration parameter defines which greedy routing protocol
     * is used to transport the messages (ie the connections between peers).
     *
     * @config
     */
    private static final String PAR_ROUTE_GREEDY = "greedy_router";

    /**
     * The {@value #PAR_ROUTE_CIRCUIT} configuration parameter defines which circuit routing protocol
     * is used to transport the messages (ie the connections between peers).
     *
     * @config
     */
    private static final String PAR_ROUTE_CIRCUIT = "circuit_router";

    private final String prefix;
    private final int linkPid, transportPid, routerGreedyPid, routerCircuitPid;

    private Address address = null;

    public DHTProtocol(String prefix) {
        this.prefix = prefix;
        this.linkPid = Configuration.getPid(prefix + "." + PAR_LINK);
        this.transportPid = Configuration.getPid(prefix + "." + PAR_TRANS);
        this.routerGreedyPid = Configuration.getPid(prefix + "." + PAR_ROUTE_GREEDY);
        this.routerCircuitPid = Configuration.getPid(prefix + "." + PAR_ROUTE_CIRCUIT);
        this.address = new Address(CommonState.r.nextDouble());
    }

    @Override
    public void processEvent(Node node, int pid, Object event) {
        String messageType = event.getClass().toString().toLowerCase();

        if(messageType.contains("greedy")) {
            ((DHTRouter) node.getProtocol(this.routerGreedyPid)).route(node, pid, this.transportPid, this.linkPid, event);
        } else if(messageType.contains("circuit")) {
            ((DHTRouter) node.getProtocol(this.routerCircuitPid)).route(node, pid, this.transportPid, this.linkPid, event);
        } else{
            System.err.println("Unknown message type");
            System.exit(1);
        }
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

}
