package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.DrlReference;
import org.springframework.stereotype.Component;

import static kungzhi.muse.osc.MessageHelper.argumentAt;

@Component
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
