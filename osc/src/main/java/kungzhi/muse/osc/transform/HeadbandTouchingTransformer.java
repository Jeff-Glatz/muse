package kungzhi.muse.osc.transform;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.HeadbandTouching;
import kungzhi.muse.osc.service.Transformer;

import static kungzhi.muse.osc.service.MessagePath.HEADBAND_TOUCHING;
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
