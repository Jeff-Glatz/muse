package kungzhi.muse.osc.transform;

import com.illposed.osc.OSCMessage;
import kungzhi.muse.model.Eeg;
import kungzhi.muse.runtime.Transformer;

import static kungzhi.muse.osc.service.MessageAddress.EEG;
import static kungzhi.muse.osc.transform.MessageHelper.extractArguments;

@Transformer(EEG)
public class EegTransformer
        implements MessageTransformer<Eeg> {

    @Override
    public Eeg fromMessage(long time, OSCMessage message)
            throws Exception {
        return new Eeg(time, extractArguments(message));
    }
}
