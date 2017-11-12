package kungzhi.muse.ui;

import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.HeadbandStatus;
import kungzhi.muse.model.SingleValue;
import kungzhi.muse.osc.service.Stream;
import kungzhi.muse.osc.service.StreamComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static kungzhi.muse.osc.service.MessagePath.HEADBAND_ON;
import static kungzhi.muse.osc.service.MessagePath.HEADBAND_STATUS;

@StreamComponent
public class HeadbandStatusUi {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Stream(path = HEADBAND_STATUS)
    public void on(Headband headband, HeadbandStatus status)
            throws Exception {
        Configuration configuration = headband.getConfiguration();
        configuration.getEegChannelLayout()
                .forEach(channel -> {
                    log.info("{}: {}", channel.getSensor(), status.forChannel(channel));
                });
    }

    @Stream(path = HEADBAND_ON)
    public void on(Headband headband, SingleValue<Integer> value)
            throws Exception {
        log.info("Touching: {}", value.get());
    }
}
