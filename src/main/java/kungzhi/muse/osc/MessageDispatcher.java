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
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.SocketAddress;
import java.util.*;

import static de.sciss.net.OSCPacket.printTextOn;
import static java.lang.String.format;

@Component
public class MessageDispatcher
        implements Session, OSCListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Set<String> availablePaths = new HashSet<>();
    private final Map<String, MessageTransformer<? extends Model>> transformers = new HashMap<>();
    private final Map<String, ModelStream<? extends Model>> streams = new HashMap<>();
    private final List<SessionListener> sessionListeners = new ArrayList<>();

    private final Configuration configuration;

    MessageDispatcher(Configuration configuration) {
        this.configuration = configuration;
    }

    public MessageDispatcher() {
        this(new Configuration());
    }

    public <M extends Model> MessageDispatcher withTransformer(
            String path, Class<M> type, MessageTransformer<M> transformer) {
        transformers.put(path, transformer);
        return this;
    }

    public <M extends Model> MessageDispatcher withTransformer(
            Path path, Class<M> type, MessageTransformer<M> transformer) {
        return withTransformer(path.getPath(), type, transformer);
    }

    public <M extends Model> MessageDispatcher withStream(
            String path, Class<M> type, ModelStream<M> stream) {
        streams.put(path, stream);
        return this;
    }

    public <M extends Model> MessageDispatcher withStream(
            Path path, Class<M> type, ModelStream<M> stream) {
        return this.withStream(path.getPath(), type, stream);
    }

    public <M extends Model> MessageDispatcher streaming(
            String path, MessageTransformer<M> transformer, ModelStream<M> stream) {
        transformers.put(path, transformer);
        streams.put(path, stream);
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
            MessageTransformer transformer = transformer(path);
            ModelStream stream = stream(path);
            availablePaths.add(path);
            stream.next(this, transformer.fromMessage(time, message));
        } catch (Exception e) {
            log.error(format("Failure dispatching message: %s", toString(message)), e);
        }
    }

    private MessageTransformer<? extends Model> transformer(String path) {
        return transformers.getOrDefault(path, (time, message) -> {
            throw new RuntimeException(format(
                    "Not configured to transform messages received on %s", path));
        });
    }

    private ModelStream<? extends Model> stream(String path) {
        return streams.getOrDefault(path, (session, model) -> {
            throw new RuntimeException(format(
                    "Not configured to stream data models received on %s", path));
        });
    }

    private static String toString(OSCMessage message) {
        ByteArrayOutputStream text = new ByteArrayOutputStream();
        printTextOn(new PrintStream(text), message);
        return text.toString();
    }
}
