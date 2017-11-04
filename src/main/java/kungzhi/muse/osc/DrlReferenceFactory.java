package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

import java.io.IOException;

import static kungzhi.muse.osc.MessageHelper.argumentAt;

public class DrlReferenceFactory
        implements SignalFactory<DrlReference> {

    @Override
    public DrlReference create(OSCMessage message)
            throws IOException {
        return new DrlReference(message.getName(),
                argumentAt(message, Float.class, 0),
                argumentAt(message, Float.class, 1));
    }
}
