package kungzhi.muse.osc;

import de.sciss.net.OSCListener;
import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Model;
import kungzhi.muse.model.Session;
import kungzhi.muse.model.SessionListener;
import kungzhi.muse.stream.ModelStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.SocketAddress;
import java.util.*;

import static de.sciss.net.OSCPacket.printTextOn;
import static java.lang.String.format;

public class MessageDispatcher
        implements Session, OSCListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Set<String> availablePaths = new HashSet<>();
    private final Map<String, MessageHandler> messageHandlers = new HashMap<>();
    private final List<SessionListener> sessionListeners = new ArrayList<>();

    private final Configuration configuration;

    public MessageDispatcher(Configuration configuration) {
        this.configuration = configuration;
    }

    public <M extends Model> MessageDispatcher withHandler(
            String path, MessageTransformer<M> transformer, ModelStream<M> stream) {
        messageHandlers.put(path, new DefaultMessageHandler<>(this, transformer, stream));
        return this;
    }

    public Set<String> availablePaths() {
        return availablePaths;
    }

    @Override
    public void addSessionListener(SessionListener listener) {
        sessionListeners.add(listener);
    }

    @Override
    public void removeSessionListener(SessionListener listener) {
        sessionListeners.remove(listener);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        if (!this.configuration.equals(configuration)) {
            Configuration previous = this.configuration.copyOf();
            this.configuration.updateFrom(configuration);
            log.info("Current configuration has been updated: {}", configuration);
            sessionListeners.forEach(listener ->
                    listener.configurationChanged(previous, this.configuration));
        }
    }

    @Override
    public void messageReceived(OSCMessage message, SocketAddress sender, long time) {
        String path = message.getName();
        try {
            MessageHandler handler = handlerForPath(path);
            availablePaths.add(path);
            handler.onMessage(time, message);
        } catch (Exception e) {
            log.error(format("Failure dispatching message: %s", toString(message)), e);
        }
    }

    private MessageHandler handlerForPath(String path) {
        return messageHandlers.getOrDefault(path, (time, message) -> {
            throw new RuntimeException(format(
                    "Not configured to handle messages received on %s", path));
        });
    }

    private static String toString(OSCMessage message) {
        ByteArrayOutputStream text = new ByteArrayOutputStream();
        printTextOn(new PrintStream(text), message);
        return text.toString();
    }
}
