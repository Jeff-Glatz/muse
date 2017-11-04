package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

import static kungzhi.muse.osc.MessageHelper.argumentAt;

public class SimpleSignalFactory
        implements SignalFactory<SimpleSignal> {

    @Override
    public SimpleSignal create(OSCMessage message)
            throws Exception {
        return new SimpleSignal(message.getName(),
                argumentAt(message, Float.class, 0));
    }
}
