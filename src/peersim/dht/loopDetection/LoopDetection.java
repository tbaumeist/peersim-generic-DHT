package peersim.dht.loopDetection;

import peersim.core.Node;

public interface LoopDetection {
	boolean addVisitedNode(Node node);
}
