package peersim.dht.message;

import java.util.LinkedList;

import peersim.core.Node;

public class DHTPath extends LinkedList<PathEntry> {

	public int getPathLength(){
		return this.size() - 1;
	}
	
	public PathEntry getSource(){
		return this.getFirst();
	}
	
	public PathEntry getDestination() {
		return this.getLast();
	}
	
	public PathEntry getPreviousNode() {
		if (this.getPathLength() < 1)
			return null;
		return this.get(this.getPathLength() - 1);
	}

	public DHTPath reverse(){
		DHTPath path = new DHTPath();
		for(PathEntry n : this){
			path.add(0,n);
		}
		return path;
	}
}
