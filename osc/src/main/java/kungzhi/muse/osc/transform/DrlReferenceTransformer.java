package kungzhi.muse.osc.transform;

import com.illposed.osc.OSCMessage;
import kungzhi.muse.model.DrlReference;
import kungzhi.muse.runtime.Transformer;

import static kungzhi.muse.osc.service.MessageAddress.DRL_REFERENCE;
import static kungzhi.muse.osc.transform.MessageHelper.argumentAt;

@Transformer(DRL_REFERENCE)
public class DrlReferenceTransformer
        implements MessageTransformer<DrlReference> {

    @Override
    public DrlReference fromMessage(long time, OSCMessage message)
            throws Exception {
        return new DrlReference(time)
                .withDrivenRightLegVoltage(argumentAt(message, Float.class, 0))
                .withReferenceVoltage(argumentAt(message, Float.class, 1));
    }
}
