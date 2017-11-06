package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

import static kungzhi.muse.osc.OSCMessageHelper.argumentAt;

public class BatteryFactory
        implements SignalFactory<Battery> {

    @Override
    public Battery create(OSCMessage message)
            throws Exception {
        return new Battery(message.getName(),
                argumentAt(message, Integer.class, 0),
                argumentAt(message, Integer.class, 1),
                argumentAt(message, Integer.class, 2),
                argumentAt(message, Integer.class, 3));
    }
}
