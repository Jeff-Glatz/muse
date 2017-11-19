package kungzhi.muse.osc.transform;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.SingleValue;

import static kungzhi.muse.osc.transform.MessageHelper.argumentAt;

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
