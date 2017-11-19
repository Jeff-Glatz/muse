package kungzhi.muse.osc.transform;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Battery;
import kungzhi.muse.osc.service.Transformer;

import static kungzhi.muse.osc.service.MessagePath.BATTERY;
import static kungzhi.muse.osc.transform.MessageHelper.argumentAt;

@Transformer(BATTERY)
public class BatteryTransformer
        implements MessageTransformer<Battery> {

    @Override
    public Battery fromMessage(long time, OSCMessage message)
            throws Exception {
        return new Battery(time)
                .withStateOfCharge(argumentAt(message, Integer.class, 0))
                .withFuelGaugeVoltage(argumentAt(message, Integer.class, 1))
                .withAdcVoltage(argumentAt(message, Integer.class, 2))
                .withTemperature(argumentAt(message, Integer.class, 3));
    }
}
