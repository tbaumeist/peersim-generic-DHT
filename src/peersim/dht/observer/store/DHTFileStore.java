package peersim.dht.observer.store;

import peersim.dht.message.DHTMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.*;

/**
 * Created by baumeist on 12/18/16.
 */
public class DHTFileStore {

    private final Logger logger;
    private final String outFormat = "%d,%d,%d,%s,%s,%b,%d,%d,%s,%s\n";

    private static DHTFileStore instance = null;

    public static DHTFileStore getInstance(String filePath) throws SecurityException, IOException{
        if(DHTFileStore.instance == null)
            DHTFileStore.instance = new DHTFileStore(filePath);
        return DHTFileStore.instance;
    }

    private DHTFileStore(String filePath) throws SecurityException, IOException {
        // Use the built in Java file logging class to write to file
        this.logger = Logger.getAnonymousLogger();
        File f = new File(filePath);
        if(f.exists())
            f.delete();
        f.getParentFile().mkdirs();
        f.createNewFile();

        FileHandler fh = new FileHandler(filePath, true);
        this.logger.addHandler(fh);
        CustomRecordFormatter formatter = new CustomRecordFormatter();
        fh.setFormatter(formatter);

        this.logger.info("ID,Source,Destination,Target,Type,Delivered,Search_Path_Length,Connection_Path_Length,Search_Path,Connection_Path\n");
    }

    public void storeData(DHTMessage m){
        // Dump the message
        this.logger.info(String.format(this.outFormat, m.getMessageID(),
                    m.getSourceNode().getID(), m.getDestinationNode().getID(),
                    m.getTarget(), m.getClass().toString(), m.wasDelivered(),
                    m.getRoutingPath().getPathLength(), m.getConnectionPath().getPathLength(),
                    m.buildPathString(m.getRoutingPath()),
                    m.buildPathString(m.getConnectionPath())));
    }

    private class CustomRecordFormatter extends Formatter {

        @Override
        public String format(final LogRecord r) {
            return r.getMessage();
        }
    }
}
