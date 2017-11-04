package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

public class MessageHelper {

    public static <T> T argumentAt(OSCMessage message, Class<T> type, int index) {
        return type.cast(message.getArg(index));
    }
}
