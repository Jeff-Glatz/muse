package kungzhi.muse.osc.transform;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Eeg;

import static kungzhi.muse.osc.transform.MessageHelper.extractArguments;

public class EegTransformer
        implements MessageTransformer<Eeg> {

    @Override
    public Eeg fromMessage(long time, OSCMessage message)
            throws Exception {
        return new Eeg(time, extractArguments(message, Float.class));
    }
}
