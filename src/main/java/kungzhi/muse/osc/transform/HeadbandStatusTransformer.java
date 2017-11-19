package kungzhi.muse.osc.transform;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.HeadbandStatus;
import kungzhi.muse.osc.service.Transformer;

import static kungzhi.muse.osc.service.MessagePath.HEADBAND_STATUS;
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
