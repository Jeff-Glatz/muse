package kungzhi.muse.osc.transform;

import com.illposed.osc.OSCMessage;
import kungzhi.muse.model.HeadbandStatus;
import kungzhi.muse.runtime.Transformer;

import static kungzhi.muse.osc.service.MessageAddress.HEADBAND_STATUS;
import static kungzhi.muse.osc.transform.MessageHelper.extractArguments;

@Transformer(HEADBAND_STATUS)
public class HeadbandStatusTransformer
        implements MessageTransformer<HeadbandStatus> {

    @Override
    public HeadbandStatus fromMessage(long time, OSCMessage message)
            throws Exception {
        return new HeadbandStatus(time, extractArguments(message, Float.class));
    }
}
