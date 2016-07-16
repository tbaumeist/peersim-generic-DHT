package peersim.dht.router;

import peersim.config.Configuration;
import peersim.core.Node;
import peersim.core.Protocol;
import peersim.dht.loopDetection.GUIDLoopDetection;
import peersim.dht.loopDetection.LoopDetection;

public abstract class DHTRouter implements Protocol {

    /**
     * The {@value #PAR_LOOP} configuration parameter defines which loop
     * detection protocol message sent by the simulator should use:
     * defaults to the {@link peersim.dht.loopDetection.GUIDLoopDetection}
     * class.
     *
     * @config
     */
    private static final String PAR_LOOP = "loop";

    /**
     * The {@value #PAR_BACK} configuration parameter defines if
     * the routing protocol should back track if it gets stuck
     * in a local minimum:
     * defaults to the value true.
     *
     * @config
     */
    private static final String PAR_BACK = "backtrack";

    protected final String prefix;
    protected final Boolean canBackTrack;

    private LoopDetection loop = null;

    public DHTRouter(String prefix) {
        this.prefix = prefix;
        this.canBackTrack = Configuration.getBoolean(prefix+"."+PAR_BACK, true);
    }

    public abstract void route(Node node, int pid, int transportPid, int linkPid, Object event);

    /**
     * Replicate this object by returning an identical copy.<br>
     * It is called by the initializer and do not fill any particular field.
     *
     * @return Object
     */
    public abstract Object clone();

    protected LoopDetection getLoopDetection(){
        if(this.loop != null)
            return this.loop;

        try {
            // Load the configured loop detection protocol
            this.loop = (LoopDetection) Configuration.getInstance(this.prefix + "."
                    + PAR_LOOP, new GUIDLoopDetection(""));

        } catch (Exception e) {
            System.err.println(String.format(
                    "Error loading a loop protocol: %s", e.getMessage()));
            System.exit(6); // abort
        }
        return this.loop;
    }
}
