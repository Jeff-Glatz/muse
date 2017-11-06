package kungzhi.muse.osc;

import com.google.protobuf.GeneratedMessageLite;
import com.jsoniter.any.Any;
import de.sciss.net.OSCMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.jsoniter.JsonIterator.deserialize;
import static kungzhi.muse.osc.MessageHelper.argumentAt;

public abstract class JsonMuseSignalTransformer<Payload extends GeneratedMessageLite<?, ?>>
        implements MuseSignalTransformer<Payload> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public final MuseSignal<Payload> fromOsc(OSCMessage message)
            throws Exception {
        Any json = deserialize(argumentAt(message, String.class, 0));
        return fromOsc(message.getName(), json);
    }

    protected abstract MuseSignal<Payload> fromOsc(String path, Any json)
            throws Exception;
}
