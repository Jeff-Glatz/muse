package kungzhi.muse.osc;

import com.jsoniter.any.Any;
import de.sciss.net.OSCMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.jsoniter.JsonIterator.deserialize;
import static kungzhi.muse.osc.MessageHelper.argumentAt;

public abstract class JsonSignalFactory<S extends Signal>
        implements SignalFactory<S> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public final S create(OSCMessage message)
            throws Exception {
        Any json = deserialize(argumentAt(message, String.class, 0));
        return create(message.getName(), json);
    }

    protected abstract S create(String path, Any json)
            throws Exception;
}
