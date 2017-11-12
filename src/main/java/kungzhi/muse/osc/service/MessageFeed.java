package kungzhi.muse.osc.service;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.EmptyModelStream;
import kungzhi.muse.model.Model;
import kungzhi.muse.model.ModelStream;
import kungzhi.muse.model.Headband;
import kungzhi.muse.osc.transform.EmptyMessageTransformer;
import kungzhi.muse.osc.transform.MessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageFeed<M extends Model> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MessagePath path;

    private MessageTransformer<M> transformer;
    private ModelStream<M> stream;

    public MessageFeed(MessagePath path) {
        this(path, new EmptyMessageTransformer<>(), new EmptyModelStream<>());
    }

    public MessageFeed(MessagePath path, MessageTransformer<M> transformer) {
        this(path, transformer, new EmptyModelStream<>());
    }

    public MessageFeed(MessagePath path, MessageTransformer<M> transformer, ModelStream<M> stream) {
        this.path = path;
        this.transformer = transformer;
        this.stream = stream;
    }

    public MessagePath getPath() {
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

    public M next(Headband headband, long time, OSCMessage message)
            throws Exception {
        M model = transformer.fromMessage(time, message);
        stream.next(headband, model);
        return model;
    }
}
