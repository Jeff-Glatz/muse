package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.controller.Controller;
import kungzhi.muse.model.Session;

import java.io.Serializable;

class DefaultMessageHandler<Model extends Serializable>
        implements MessageHandler {
    private final Session session;
    private final MessageTransformer<Model> transformer;
    private final Controller<Model> controller;

    public DefaultMessageHandler(Session session,
                                 MessageTransformer<Model> transformer,
                                 Controller<Model> controller) {
        this.session = session;
        this.transformer = transformer;
        this.controller = controller;
    }

    @Override
    public void onMessage(OSCMessage message, long time)
            throws Exception {
        controller.update(session,
                transformer.fromMessage(message, time));
    }
}
