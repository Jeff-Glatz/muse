package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Accelerometer;

import static kungzhi.muse.osc.MessageHelper.argumentAt;

public class AccelerometerTransformer
        implements MessageTransformer<Accelerometer> {

    @Override
    public Accelerometer fromMessage(long time, OSCMessage message)
            throws Exception {
        return new Accelerometer(time,
                argumentAt(message, Float.class, 0),
                argumentAt(message, Float.class, 1),
                argumentAt(message, Float.class, 2));
    }
}
