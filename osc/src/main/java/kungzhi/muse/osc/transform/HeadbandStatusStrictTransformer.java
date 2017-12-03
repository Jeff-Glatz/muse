package kungzhi.muse.osc.transform;

import com.illposed.osc.OSCMessage;
import kungzhi.muse.model.HeadbandStatusStrict;
import kungzhi.muse.runtime.Transformer;

import static kungzhi.muse.osc.service.MessageAddress.HEADBAND_STATUS_STRICT;
import static kungzhi.muse.osc.transform.MessageHelper.extractArguments;

@Transformer(HEADBAND_STATUS_STRICT)
public class HeadbandStatusStrictTransformer
        implements MessageTransformer<HeadbandStatusStrict> {

    @Override
    public HeadbandStatusStrict fromMessage(long time, OSCMessage message)
            throws Exception {
        return new HeadbandStatusStrict(time, extractArguments(message, Float.class));
    }
}
