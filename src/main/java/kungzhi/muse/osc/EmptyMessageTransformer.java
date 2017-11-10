package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Model;

public class EmptyMessageTransformer<M extends Model>
        implements MessageTransformer<M> {

    @Override
    public M fromMessage(long time, OSCMessage message)
            throws Exception {
        return null;
    }
}
