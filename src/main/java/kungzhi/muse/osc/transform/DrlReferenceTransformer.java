package kungzhi.muse.osc.transform;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.DrlReference;

import static kungzhi.muse.osc.transform.MessageHelper.argumentAt;

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
