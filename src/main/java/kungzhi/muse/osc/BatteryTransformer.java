package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Battery;
import org.springframework.stereotype.Component;

import static kungzhi.muse.osc.MessageHelper.argumentAt;

@Component
public class BatteryTransformer
        implements MessageTransformer<Battery> {

    @Override
    public Battery fromMessage(long time, OSCMessage message)
            throws Exception {
        return new Battery(time,
                argumentAt(message, Integer.class, 0),
                argumentAt(message, Integer.class, 1),
                argumentAt(message, Integer.class, 2),
                argumentAt(message, Integer.class, 3));
    }
}
