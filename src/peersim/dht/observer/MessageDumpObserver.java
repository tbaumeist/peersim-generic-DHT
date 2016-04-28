package peersim.dht.observer;

import java.io.PrintStream;
import java.util.List;

import peersim.core.Node;
import peersim.dht.DHTProtocol;
import peersim.dht.message.DHTMessage;

/**
 * Observers and outputs the status of every message sent by the simulator. It
 * expects a protocol that implements {@link peersim.dht.DHTProtocol}.
 * 
 * @author todd
 * 
 */
public class MessageDumpObserver extends DHTPrinter {

	public MessageDumpObserver(String prefix) {
		super(prefix, ".csv");

		// enable Message storage in the protocol
		this.getDHTProtocol().enableMessageStorage();
	}

	@Override
	protected void writeData(PrintStream out) {
		DHTProtocol dht = this.getDHTProtocol();
		// Write the CSV header
		out.println("ID,Source,Destination,Target,Type,Delivered,Search_Path_Length,Connection_Path_Length,Search_Path,Connection_Path");

		// Dump the messages
		String outFormat = "%d,%d,%d,%f,%s,%b,%d,%d,%s,%s";
		for (DHTMessage m : dht.getAllMessages()) {
			out.println(String.format(outFormat, m.getMessageID(), 
					m.getSourceNode().getID(), m.getDestinationNode().getID(), 
					m.getTarget(), m.getClass().toString(), m.getDelivered(), 
					m.getRoutingPath().getPathLength(), m.getConnectionPath().getPathLength(),
					this.buildPath(m.getRoutingPath()), 
					this.buildPath(m.getConnectionPath())));
		}
		dht.clearMessages();
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
