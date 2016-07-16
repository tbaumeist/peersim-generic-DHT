package peersim.dht.observer;

import java.io.PrintStream;
import java.util.List;

import peersim.core.Node;
import peersim.dht.message.DHTMessage;

public class MessageDumpObserver extends DHTPrinter {

	public MessageDumpObserver(String prefix) {
		super(prefix, ".csv");

		// enable Message storage in the protocol
		this.getDataStore().enable();
	}

	@Override
	protected void writeData(PrintStream out) {
		// Write the CSV header
		out.println("ID,Source,Destination,Target,Type,Delivered,Search_Path_Length,Connection_Path_Length,Search_Path,Connection_Path");

		// Dump the messages
		String outFormat = "%d,%d,%d,%s,%s,%b,%d,%d,%s,%s";
		for (DHTMessage m : this.getDataStore().getAllMessages()) {
			out.println(String.format(outFormat, m.getMessageID(), 
					m.getSourceNode().getID(), m.getDestinationNode().getID(), 
					m.getTarget(), m.getClass().toString(), m.getDelivered(), 
					m.getRoutingPath().getPathLength(), m.getConnectionPath().getPathLength(),
					this.buildPath(m.getRoutingPath()), 
					this.buildPath(m.getConnectionPath())));
		}
		this.getDataStore().clearMessages();
	}

	private String buildPath(List<Node> path) {
		StringBuilder b = new StringBuilder();
		for (Node n : path) {
			b.append(n.getID());
			b.append(">");
		}
		if (b.length() < 1)
			return "";
		return b.substring(0, b.length() - 1);
	}

}
