package peersim.dht;

import org.junit.Test;
import peersim.dht.base.Loader;

import static org.junit.Assert.*;

/**
 * Created by baumeist on 5/14/17.
 */
public class DHTProtocolRoutingFailTest extends Loader {
    @Test
    public void test() throws Exception {
        initSimulator(Loader.BASE_PATH + "message_failure.cfg");
    }

}