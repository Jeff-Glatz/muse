package kungzhi.muse.ui;

import kungzhi.muse.model.Battery;
import kungzhi.muse.model.Headband;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BatteryUi {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Headband headband;

    @Autowired
    public BatteryUi(Headband headband) {
        this.headband = headband;
    }

    @PostConstruct
    public void initialize()
            throws Exception {
        Battery battery = headband.getBattery();
        battery.addActiveItemListener((current, previous) -> {
            updateUi();
        });
    }

    private void updateUi() {
        Battery battery = headband.getBattery();
        log.info("Battery: {}%", battery.getPercentRemaining());

    }
}
