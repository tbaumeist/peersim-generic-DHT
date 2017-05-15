package peersim.dht.routingtable;

import org.junit.Test;
import org.junit.runner.RunWith;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dht.base.Loader;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by baumeist on 5/14/17.
 */
public class NHopRoutingTableOneHopTest extends Loader {

    @Test
    public void getRoutingTableEntries() throws Exception {
        this.initSimulator(Loader.BASE_PATH + "routing_table_1.cfg");
        assertTrue(CommonState.r.getLastSeed() == 1234567890);
        assertTrue(Network.size() == 14);

        int routingTableId = Configuration.getPid("protocol.generic_dht.routing_table");
        int topologyId = Configuration.getPid("protocol.generic_dht.topology");

        // check the first node
        Node n = Network.get(0);
        NHopRoutingTable rt = (NHopRoutingTable)n.getProtocol(routingTableId);
        List<DHTRoutingTable.RoutingTableEntry> entries = rt.getRoutingTableEntries(n, topologyId);
        int[] node0peers = new int[]{2,10,11,13};
        for(DHTRoutingTable.RoutingTableEntry e : entries) {
            assertTrue(this.contains(node0peers, e.routeToNode.getIndex()));
            assertTrue(this.contains(node0peers, e.targetNode.getIndex()));
        }
    }
}