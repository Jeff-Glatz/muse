package kungzhi.muse.ui;

import kungzhi.muse.model.Battery;
import kungzhi.muse.model.Headband;
import kungzhi.muse.osc.service.StreamHandler;
import kungzhi.muse.osc.service.StreamComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static kungzhi.muse.osc.service.MessagePath.BATTERY;

@StreamComponent
public class BatteryUi {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Battery battery = new Battery();

    @StreamHandler(BATTERY)
    public void on(Headband headband, Battery battery)
            throws Exception {
        if (this.battery.updateFrom(battery)) {
            log.info("Battery: {}%", battery.getPercentRemaining());
        }
    }
}
