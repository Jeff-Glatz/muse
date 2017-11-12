package kungzhi.muse.osc.transform;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Values;

import java.lang.reflect.Field;

public class MessageHelper {
    private static final Field args = messageField("args");

    public static <T> T argumentAt(OSCMessage message, Class<T> type, int index) {
        return type.cast(message.getArg(index));
    }

    public static <T> Values<T> extractArguments(OSCMessage message, Class<T> type) {
        return new Values<>(type, args(message));
    }

    public static Object[] args(OSCMessage message) {
        try {
            return (Object[]) MessageHelper.args.get(message);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Field messageField(String name) {
        try {
            Field args = OSCMessage.class.getDeclaredField(name);
            args.setAccessible(true);
            return args;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
