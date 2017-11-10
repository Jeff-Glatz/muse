package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.SingleValue;

import static kungzhi.muse.osc.MessageHelper.argumentAt;

public class SingleValueTransformer<V>
        implements MessageTransformer<SingleValue<V>> {
    private final Class<V> type;

    public SingleValueTransformer(Class<V> type) {
        this.type = type;
    }

    @Override
    public SingleValue<V> fromMessage(long time, OSCMessage message)
            throws Exception {
        return new SingleValue<>(time, argumentAt(message, type, 0));
    }
}
