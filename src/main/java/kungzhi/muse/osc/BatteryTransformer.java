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
        return new Battery(time)
                .withStateOfCharge(argumentAt(message, Integer.class, 0))
                .withFuelGaugeVoltage(argumentAt(message, Integer.class, 1))
                .withAdcVoltage(argumentAt(message, Integer.class, 2))
                .withTemperature(argumentAt(message, Integer.class, 3));
    }
}
