package kungzhi.muse.osc.transform;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Accelerometer;
import kungzhi.muse.osc.service.Transformer;

import static kungzhi.muse.osc.service.MessagePath.ACCELEROMETER;
import static kungzhi.muse.osc.transform.MessageHelper.argumentAt;

@Transformer(ACCELEROMETER)
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
