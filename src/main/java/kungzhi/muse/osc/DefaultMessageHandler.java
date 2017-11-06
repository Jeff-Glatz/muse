package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Model;
import kungzhi.muse.model.Session;
import kungzhi.muse.stream.ModelStream;

class DefaultMessageHandler<M extends Model>
        implements MessageHandler {
    private final Session session;
    private final MessageTransformer<M> transformer;
    private final ModelStream<M> stream;

    public DefaultMessageHandler(Session session,
                                 MessageTransformer<M> transformer,
                                 ModelStream<M> stream) {
        this.session = session;
        this.transformer = transformer;
        this.stream = stream;
    }

    @Override
    public void onMessage(long time, OSCMessage message)
            throws Exception {
        stream.next(session, transformer.fromMessage(time, message));
    }
}
