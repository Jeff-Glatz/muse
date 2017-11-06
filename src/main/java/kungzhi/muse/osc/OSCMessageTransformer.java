package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

import java.io.Serializable;

public interface OSCMessageTransformer<D extends Serializable> {
    D fromMessage(OSCMessage message)
            throws Exception;
}
