package peersim.dht.loopDetection;

import java.util.LinkedList;
import java.util.List;

import peersim.core.Node;
import peersim.core.Protocol;

public class GUIDLoopDetection implements Protocol, LoopDetection {
	private final String prefix;
	private List<Node> alreadyRouted = new LinkedList<Node>();
	
	public GUIDLoopDetection(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * Replicate this object by returning an identical copy.<br>
	 * It is called by the initializer and do not fill any particular field.
	 * 
	 * @return Object
	 */
	public Object clone() {
		GUIDLoopDetection dolly = new GUIDLoopDetection(this.prefix);
		return dolly;
	}

	@Override
	public boolean addVisitedNode(Node node) {
		if( this.alreadyRouted.contains(node))
			return false;
		this.alreadyRouted.add(node);
		return true;
	}
}
