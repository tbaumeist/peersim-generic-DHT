package peersim.dht.message;

import java.util.LinkedList;

import peersim.core.Node;

public class DHTPath extends LinkedList<Node> {

	public int getPathLength(){
		return this.size() - 1;
	}
	
	public Node getSource(){
		return this.getFirst();
	}
	
	public Node getDestination() {
		return this.getLast();
	}
	
	public Node getPreviousNode() {
		if (this.getPathLength() < 1)
			return null;
		return this.get(this.getPathLength() - 1);
	}

	public DHTPath reverse(){
		DHTPath path = new DHTPath();
		for(Node n : this){
			path.add(0,n);
		}
		return path;
	}
}
