package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

public interface SignalFactory<S extends Signal> {
    S create(OSCMessage message)
            throws Exception;
}
