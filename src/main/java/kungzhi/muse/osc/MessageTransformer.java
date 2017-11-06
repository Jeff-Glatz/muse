package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Model;

public interface MessageTransformer<M extends Model> {
    M fromMessage(long time, OSCMessage message)
            throws Exception;
}
