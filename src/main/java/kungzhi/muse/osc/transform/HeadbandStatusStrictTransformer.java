package kungzhi.muse.osc.transform;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.HeadbandStatusStrict;

import static kungzhi.muse.osc.transform.MessageHelper.extractArguments;

public class HeadbandStatusStrictTransformer
        implements MessageTransformer<HeadbandStatusStrict> {

    @Override
    public HeadbandStatusStrict fromMessage(long time, OSCMessage message)
            throws Exception {
        return new HeadbandStatusStrict(time, extractArguments(message, Float.class));
    }
}
