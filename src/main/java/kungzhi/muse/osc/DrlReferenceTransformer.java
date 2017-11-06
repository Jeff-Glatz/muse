package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.DrlReference;

import static kungzhi.muse.osc.MessageHelper.argumentAt;

public class DrlReferenceTransformer
        implements MessageTransformer<DrlReference> {
    @Override
    public DrlReference fromMessage(long time, OSCMessage message)
            throws Exception {
        return new DrlReference(time,
                argumentAt(message, Float.class, 0),
                argumentAt(message, Float.class, 1));
    }
}
