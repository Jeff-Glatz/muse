package kungzhi.muse.osc;

import de.sciss.net.OSCListener;
import de.sciss.net.OSCMessage;
import kungzhi.muse.controller.Controller;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Session;
import kungzhi.muse.model.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.SocketAddress;
import java.util.*;

import static de.sciss.net.OSCPacket.printTextOn;
import static java.lang.String.format;

@Resource
public class MessageDispatcher
        implements Session, OSCListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Set<String> availablePaths = new HashSet<>();
    private final Map<String, MessageHandler> messageHandlers = new HashMap<>();
    private final List<SessionListener> sessionListeners = new ArrayList<>();

    private final Configuration currentConfiguration;

    public MessageDispatcher(Configuration currentConfiguration) {
        this.currentConfiguration = currentConfiguration;
    }

    public <Model extends Serializable> MessageDispatcher withHandler(
            String path, MessageTransformer<Model> transformer, Controller<Model> controller) {
        messageHandlers.put(path, new DefaultMessageHandler<>(this, transformer, controller));
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
    public Configuration currentConfiguration() {
        return currentConfiguration;
    }

    @Override
    public void currentConfiguration(Configuration configuration) {
        if (!currentConfiguration.equals(configuration)) {
            Configuration previous = currentConfiguration.copyOf();
            currentConfiguration.updateFrom(configuration);
            log.info("Current configuration has been updated");
            sessionListeners.forEach(listener ->
                    listener.configurationChanged(previous, currentConfiguration));
        }
    }

    @Override
    public void messageReceived(OSCMessage message, SocketAddress sender, long time) {
        String path = message.getName();
        try {
            MessageHandler processor = processorForPath(path);
            availablePaths.add(path);
            processor.onMessage(message, time);
        } catch (Exception e) {
            log.error(format("Failure dispatching message: %s", toString(message)), e);
        }
    }

    private MessageHandler processorForPath(String path) {
        return messageHandlers.getOrDefault(path, (message, time) -> {
            throw new RuntimeException(format(
                    "Not configured to dispatch messages received on %s", path));
        });
    }

    private static String toString(OSCMessage message) {
        ByteArrayOutputStream text = new ByteArrayOutputStream();
        printTextOn(new PrintStream(text), message);
        return text.toString();
    }
}
