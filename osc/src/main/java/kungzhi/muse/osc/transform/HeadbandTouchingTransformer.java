package kungzhi.muse.osc.transform;

import com.illposed.osc.OSCMessage;
import kungzhi.muse.model.HeadbandTouching;
import kungzhi.muse.runtime.Transformer;

import static kungzhi.muse.osc.service.MessageAddress.HEADBAND_TOUCHING;
import static kungzhi.muse.osc.transform.MessageHelper.argumentAt;

@Transformer(HEADBAND_TOUCHING)
public class HeadbandTouchingTransformer
        implements MessageTransformer<HeadbandTouching> {

    @Override
    public HeadbandTouching fromMessage(long time, OSCMessage message)
            throws Exception {
        return new HeadbandTouching(time, argumentAt(message, Integer.class, 0));
    }
}
