package kungzhi.muse.osc.service;

import de.sciss.net.OSCListener;
import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.Model;
import kungzhi.muse.model.ModelStream;
import kungzhi.muse.osc.transform.MessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.SocketAddress;
import java.time.Clock;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static de.sciss.net.OSCPacket.printTextOn;
import static java.lang.String.format;

/**
 * A singleton that listens for OSC messages, transforms them into the appropriate data model,
 * then streams the results to consumers.
 *
 * @see MessageTransformer
 * @see TransformerPostProcessor
 * @see ModelStream
 * @see StreamPostProcessor
 */
@Component
public class MessageDispatcher
        implements OSCListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ErrorHandler<Throwable> defaultErrorHandler = (dispatcher, message, error) -> {
    };
    private final Set<String> availablePaths = new HashSet<>();
    private final Map<Class<? extends Throwable>, ErrorHandler<? extends Throwable>> handlers = new HashMap<>();
    private final Map<String, MessageTransformer<? extends Model>> transformers = new HashMap<>();
    private final Map<String, ModelStream<? extends Model>> streams = new HashMap<>();
    private final Clock clock;
    private final Headband headband;

    public MessageDispatcher(Clock clock, Headband headband) {
        this.clock = clock;
        this.headband = headband;
    }

    public <M extends Model> MessageDispatcher withTransformer(
            String path, Class<M> type, MessageTransformer<M> transformer) {
        transformers.put(path, transformer);
        return this;
    }

    public <M extends Model> MessageDispatcher withTransformer(
            MessagePath path, Class<M> type, MessageTransformer<M> transformer) {
        return withTransformer(path.getName(), type, transformer);
    }

    public <M extends Model> MessageDispatcher withStream(
            String path, Class<M> type, ModelStream<M> stream) {
        streams.put(path, stream);
        return this;
    }

    public <M extends Model> MessageDispatcher withStream(
            MessagePath path, ModelStream<M> stream) {
        return withStream(path.getName(), null, stream);
    }

    public <M extends Model> MessageDispatcher withStream(
            MessagePath path, Class<M> type, ModelStream<M> stream) {
        return withStream(path.getName(), type, stream);
    }

    public <M extends Model> MessageDispatcher streaming(
            String path, MessageTransformer<M> transformer, ModelStream<M> stream) {
        transformers.put(path, transformer);
        streams.put(path, stream);
        return this;
    }

    public <M extends Model> MessageDispatcher streaming(
            MessagePath path, MessageTransformer<M> transformer, ModelStream<M> stream) {
        return streaming(path.getName(), transformer, stream);
    }

    public <E extends Exception> MessageDispatcher handling(Class<E> type, ErrorHandler<E> errorHandler) {
        this.handlers.put(type, errorHandler);
        return this;
    }

    public Set<String> availablePaths() {
        return availablePaths;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void messageReceived(OSCMessage message, SocketAddress sender, long time) {
        String path = message.getName();
        try {
            MessageTransformer transformer = transformer(path);
            ModelStream stream = stream(path);
            availablePaths.add(path);
            stream.next(headband, transformer.fromMessage(clock.millis(), message));
        } catch (MissingTransformerException e) {
            log.debug("No transformer configured for {}", path);
            this.<MissingTransformerException>handler(e).on(this, message, e);
        } catch (MissingStreamException e) {
            log.debug("No stream configured for {}", path);
            this.<MissingStreamException>handler(e).on(this, message, e);
        } catch (Throwable e) {
            log.error(format("Failure dispatching message: %s", toString(message)), e);
            handler(e).on(this, message, e);
        }
    }

    private MessageTransformer<? extends Model> transformer(String path) {
        return transformers.getOrDefault(path, (time, message) -> {
            throw new MissingTransformerException(format(
                    "Not configured to transform messages received on %s", path), path);
        });
    }

    private ModelStream<? extends Model> stream(String path) {
        return streams.getOrDefault(path, (session, model) -> {
            throw new MissingStreamException(format(
                    "Not configured to stream data models received on %s", path), path);
        });
    }

    @SuppressWarnings("unchecked")
    private <E extends Throwable> ErrorHandler<E> handler(Throwable throwable) {
        return (ErrorHandler<E>) handlers.getOrDefault(throwable.getClass(),
                defaultErrorHandler);
    }

    private static String toString(OSCMessage message) {
        ByteArrayOutputStream text = new ByteArrayOutputStream();
        printTextOn(new PrintStream(text), message);
        return text.toString();
    }
}
