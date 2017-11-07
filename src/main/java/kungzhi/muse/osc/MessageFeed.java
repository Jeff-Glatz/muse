package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Model;
import kungzhi.muse.model.ModelStream;
import kungzhi.muse.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageFeed<M extends Model> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Path path;

    private MessageTransformer<M> transformer;
    private ModelStream<M> stream;

    public MessageFeed(Path path) {
        this(path, (time, message) -> null, (session, m) -> {
        });
    }

    public MessageFeed(Path path, MessageTransformer<M> transformer) {
        this(path, transformer, (session, m) -> {
        });
    }

    public MessageFeed(Path path, MessageTransformer<M> transformer, ModelStream<M> stream) {
        this.path = path;
        this.transformer = transformer;
        this.stream = stream;
    }

    public Path getPath() {
        return path;
    }

    public MessageFeed<M> withTransformer(MessageTransformer<M> transformer) {
        this.transformer = transformer;
        return this;
    }

    public MessageFeed<M> withStream(ModelStream<M> stream) {
        this.stream = stream;
        return this;
    }

    public M next(Session session, long time, OSCMessage message)
            throws Exception {
        M model = transformer.fromMessage(time, message);
        stream.next(session, model);
        return model;
    }
}
