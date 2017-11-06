package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

public interface MessageHandler {
    void onMessage(OSCMessage message, long time)
            throws Exception;
}
