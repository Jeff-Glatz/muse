package kungzhi.muse.osc.service;

import de.sciss.net.OSCMessage;

public interface MessageDispatcherErrorHandler<E extends Throwable> {
    void on(MessageDispatcher dispatcher, OSCMessage message, E error);
}
