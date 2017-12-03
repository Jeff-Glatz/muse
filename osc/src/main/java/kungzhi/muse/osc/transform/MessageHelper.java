package kungzhi.muse.osc.transform;

import com.illposed.osc.OSCMessage;
import kungzhi.muse.model.Values;

import java.lang.reflect.Field;
import java.util.List;

public class MessageHelper {
    private static final Field args = messageField("arguments");

    public static <T> T argumentAt(OSCMessage message, Class<T> type, int index) {
        List<Object> arguments = message.getArguments();
        return type.cast(arguments.get(index));
    }

    public static <T> Values<T> extractArguments(OSCMessage message, Class<T> type) {
        return new Values<>(type, args(message));
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

    private static List<Object> args(OSCMessage message) {
        try {
            return (List<Object>) MessageHelper.args.get(message);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
