package kungzhi.muse.osc.service;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.Model;
import kungzhi.muse.model.ModelStream;
import kungzhi.muse.osc.transform.MessageTransformer;
import kungzhi.muse.runtime.StreamPostProcessor;
import kungzhi.muse.runtime.TransformerPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

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
    private final MessageDispatcherErrorHandler<Throwable> defaultErrorHandler = (dispatcher, message, error) -> {
    };
    private final Map<Class<? extends Throwable>, MessageDispatcherErrorHandler<? extends Throwable>> handlers = new HashMap<>();
    private final Map<String, MessageTransformer> transformers = new HashMap<>();
    private final Map<String, List<ModelStream>> streams = new HashMap<>();
    private final Clock clock;
    private final Executor executor;
    private final Headband headband;

    @Autowired
    public MessageDispatcher(Clock clock, Executor executor, Headband headband) {
        this.clock = clock;
        this.executor = executor;
        this.headband = headband;
    }

    public <M extends Model> MessageDispatcher withTransformer(
            String address, Class<M> type, MessageTransformer<M> transformer) {
        transformers.put(address, transformer);
        return this;
    }

    public <M extends Model> MessageDispatcher withTransformer(
            MessageAddress address, Class<M> type, MessageTransformer<M> transformer) {
        return withTransformer(address.getName(), type, transformer);
    }

    public <M extends Model> MessageDispatcher withStream(
            String address, Class<M> type, ModelStream<M> stream) {
        streams(address, false).add(stream);
        return this;
    }

    public <M extends Model> MessageDispatcher withStream(
            MessageAddress address, ModelStream<M> stream) {
        return withStream(address.getName(), null, stream);
    }

    public <M extends Model> MessageDispatcher withStream(
            MessageAddress address, Class<M> type, ModelStream<M> stream) {
        return withStream(address.getName(), type, stream);
    }

    public <M extends Model> MessageDispatcher streaming(
            String address, MessageTransformer<M> transformer, ModelStream<M> stream) {
        transformers.put(address, transformer);
        return withStream(address, null, stream);
    }

    public <M extends Model> MessageDispatcher streaming(
            MessageAddress address, MessageTransformer<M> transformer, ModelStream<M> stream) {
        return streaming(address.getName(), transformer, stream);
    }

    public <E extends Exception> MessageDispatcher handling(Class<E> type, MessageDispatcherErrorHandler<E> errorHandler) {
        this.handlers.put(type, errorHandler);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void acceptMessage(Date time, OSCMessage message) {
        String address = message.getAddress();
        try {
            MessageTransformer transformer = transformer(address);
            List<ModelStream> streams = streams(address, true);
            Model model = transformer.fromMessage(time != null ? time.getTime() : clock.millis(), message);
            executor.execute(() -> streams.forEach(stream -> {
                try {
                    stream.next(headband, model);
                } catch (Exception e) {
                    log.error(format("Failure dispatching message: %s", toString(message)), e);
                    handler(e).on(this, message, e);
                }
            }));
        } catch (MissingTransformerException e) {
            log.debug("No transformer configured for {}", address);
            this.<MissingTransformerException>handler(e).on(this, message, e);
        } catch (MissingStreamException e) {
            log.debug("No stream configured for {}", address);
            this.<MissingStreamException>handler(e).on(this, message, e);
        } catch (Throwable e) {
            log.error(format("Failure dispatching message: %s", toString(message)), e);
            handler(e).on(this, message, e);
        }
    }

    private MessageTransformer transformer(String address) {
        return transformers.getOrDefault(address, (time, message) -> {
            throw new MissingTransformerException(format(
                    "Not configured to transform messages received on %s", address), address);
        });
    }

    private List<ModelStream> streams(String address, boolean failIfEmpty) {
        List<ModelStream> streamsForAddress = streams
                .computeIfAbsent(address, key -> new ArrayList<>());
        if (failIfEmpty && streamsForAddress.isEmpty()) {
            throw new MissingStreamException(format(
                    "Not configured to stream data models received on %s", address), address);
        }
        return streamsForAddress;
    }

    @SuppressWarnings("unchecked")
    private <E extends Throwable> MessageDispatcherErrorHandler<E> handler(Throwable throwable) {
        return (MessageDispatcherErrorHandler<E>) handlers.getOrDefault(throwable.getClass(),
                defaultErrorHandler);
    }

    private static String toString(OSCMessage message) {
        return format("address: %s args: %s", message.getAddress(), message.getArguments());
    }
}
