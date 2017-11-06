package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

import java.util.ArrayList;
import java.util.List;

public class OSCMessageHelper {

    public static <T> T argumentAt(OSCMessage message, Class<T> type, int index) {
        return type.cast(message.getArg(index));
    }

    public static <T> List<T> collectArguments(OSCMessage message) {
        List<T> arguments = new ArrayList<>(message.getArgCount());
        for (int i = 0; i < message.getArgCount(); i++) {
            arguments.add((T) message.getArg(i));
        }
        return arguments;
    }
}
