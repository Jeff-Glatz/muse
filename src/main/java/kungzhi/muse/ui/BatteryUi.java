package kungzhi.muse.ui;

import kungzhi.muse.model.Battery;
import kungzhi.muse.model.Headband;
import kungzhi.muse.osc.service.Stream;
import kungzhi.muse.osc.service.StreamComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static kungzhi.muse.osc.service.MessagePath.BATTERY;

@StreamComponent
public class BatteryUi {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Stream(path = BATTERY)
    public void on(Headband headband, Battery battery)
            throws Exception {
        log.info("Battery: {}%", battery.getPercentRemaining());
    }
}
