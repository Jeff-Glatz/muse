package kungzhi.muse.ui;

import kungzhi.muse.model.Battery;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.ModelStream;
import kungzhi.muse.osc.service.Stream;
import org.springframework.stereotype.Component;

import static kungzhi.muse.osc.service.MessagePath.BATTERY;

@Component
@Stream(path = BATTERY, type = Battery.class)
public class BatteryUi
        implements ModelStream<Battery> {

    @Override
    public void next(Headband headband, Battery battery)
            throws Exception {

    }
}
