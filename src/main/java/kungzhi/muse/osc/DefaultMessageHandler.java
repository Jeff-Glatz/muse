package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.controller.Controller;
import kungzhi.muse.model.Model;
import kungzhi.muse.model.Session;

class DefaultMessageHandler<M extends Model>
        implements MessageHandler {
    private final Session session;
    private final MessageTransformer<M> transformer;
    private final Controller<M> controller;

    public DefaultMessageHandler(Session session,
                                 MessageTransformer<M> transformer,
                                 Controller<M> controller) {
        this.session = session;
        this.transformer = transformer;
        this.controller = controller;
    }

    @Override
    public void onMessage(OSCMessage message, long time)
            throws Exception {
        controller.update(session,
                transformer.fromMessage(time, message));
    }
}
