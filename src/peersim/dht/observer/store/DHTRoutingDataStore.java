package peersim.dht.observer.store;

import peersim.dht.message.DHTMessage;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.*;

/**
 * Created by baumeist on 12/18/16.
 */
public class DHTRoutingDataStore {

    private final Logger logger;
    private final String outFormat = "%d,%d,%d,%d,%s,%s,%b,%d,%d,%s,%s\n";

    private static Hashtable<String, DHTRoutingDataStore> instances = new Hashtable<>();

    public static DHTRoutingDataStore getInstance(String filePath) throws SecurityException, IOException{
        File f = new File(filePath);
        if(!DHTRoutingDataStore.instances.containsKey(f.getCanonicalPath()))
            DHTRoutingDataStore.instances.put(f.getCanonicalPath(), new DHTRoutingDataStore(f.getCanonicalPath()));
        return DHTRoutingDataStore.instances.get(f.getCanonicalPath());
    }

    private DHTRoutingDataStore(String filePath) throws SecurityException, IOException {
        // Use the built in Java file logging class to write to file
        this.logger = Logger.getAnonymousLogger();
        this.logger.setUseParentHandlers(false);
        File f = new File(filePath);
        if(f.exists())
            f.delete();
        f.getParentFile().mkdirs();
        f.createNewFile();

        FileHandler fh = new FileHandler(filePath, true);
        this.logger.addHandler(fh);
        CustomRecordFormatter formatter = new CustomRecordFormatter();
        fh.setFormatter(formatter);

        this.logger.info("ID,Ref_ID,Source,Destination,Target,Type,Delivered,Search_Path_Length,Connection_Path_Length,Search_Path,Connection_Path\n");
    }

    public void storeData(DHTMessage m){
        // Dump the message
        this.logger.info(String.format(this.outFormat, m.getMessageID(), m.getRefMessageID(),
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
