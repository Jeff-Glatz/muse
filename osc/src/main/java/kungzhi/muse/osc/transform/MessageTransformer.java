package kungzhi.muse.osc.transform;

import com.illposed.osc.OSCMessage;
import kungzhi.muse.model.Model;

public interface MessageTransformer<M extends Model> {
    M fromMessage(long time, OSCMessage message)
            throws Exception;
}
