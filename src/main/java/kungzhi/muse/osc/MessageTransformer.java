package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

import java.io.Serializable;

public interface MessageTransformer<Model extends Serializable> {
    Model fromMessage(OSCMessage message, long time)
            throws Exception;
}
