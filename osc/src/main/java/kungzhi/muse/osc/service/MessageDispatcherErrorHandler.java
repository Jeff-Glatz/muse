package kungzhi.muse.osc.service;

import com.illposed.osc.OSCMessage;

public interface MessageDispatcherErrorHandler<E extends Throwable> {
    void on(MessageDispatcher dispatcher, OSCMessage message, E error);
}
