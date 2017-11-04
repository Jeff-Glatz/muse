package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

import java.io.IOException;

public interface SignalFactory<S extends Signal> {
    S create(OSCMessage message)
            throws Exception;
}
