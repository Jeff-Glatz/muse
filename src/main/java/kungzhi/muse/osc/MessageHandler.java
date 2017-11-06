package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

public interface MessageHandler {
    void onMessage(long time, OSCMessage message)
            throws Exception;
}
