package peersim.dht.loopDetection;

import peersim.core.Node;

public interface LoopDetection {
	public boolean addVistedNode(Node node);
	public boolean canRouteNode(Node node);
}
