package kungzhi.muse.osc.transform;

import com.illposed.osc.OSCMessage;
import kungzhi.muse.model.Accelerometer;
import kungzhi.muse.runtime.Transformer;

import static kungzhi.muse.osc.service.MessageAddress.ACCELEROMETER;
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
