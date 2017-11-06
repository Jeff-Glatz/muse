package kungzhi.muse.osc;

import de.sciss.net.OSCListener;
import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static de.sciss.net.OSCPacket.printTextOn;
import static java.lang.String.format;

public class MessageDispatcher
        implements Session, OSCListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Set<String> availablePaths = new HashSet<>();
    private final Map<String, MessageTransformer<?>> messageTransformerMap = new HashMap<>();

    private Configuration currentConfiguration;

    public MessageDispatcher withTransformer(String path, MessageTransformer<?> transformer) {
        messageTransformerMap.put(path, transformer);
        return this;
    }

    @Override
    public Configuration currentConfiguration() {
        return currentConfiguration;
    }

    public Set<String> availablePaths() {
        return availablePaths;
    }

    @Override
    public void messageReceived(OSCMessage message, SocketAddress sender, long time) {
        try {
            MessageTransformer<?> transformer = transformerForPath(message.getName());
            availablePaths.add(message.getName());
            Serializable model = transformer.fromMessage(message, time);
            log.info("Processed data: {}", model);
        } catch (Exception e) {
            log.error(format("Failure dispatching message: %s", toString(message)), e);
        }
    }

    private MessageTransformer<?> transformerForPath(String path) {
        return messageTransformerMap.getOrDefault(path, (message, time) -> {
            throw new RuntimeException(format(
                    "Not configured to dispatch message received on %s", path));
        });
    }

    private static String toString(OSCMessage message) {
        ByteArrayOutputStream text = new ByteArrayOutputStream();
        printTextOn(new PrintStream(text), message);
        return text.toString();
    }
}
