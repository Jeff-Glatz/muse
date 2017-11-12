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
    private HeadbandStatus headbandStatus = new HeadbandStatus(0, null);
    private SingleValue<Integer> headbandTouching = new SingleValue<>(0, null);

    @Stream(path = HEADBAND_STATUS)
    public void on(Headband headband, HeadbandStatus headbandStatus)
            throws Exception {
        log.info(headbandStatus.toString());
        if (!this.headbandStatus.sameAs(headbandStatus)) {
            this.headbandStatus = headbandStatus;
            Configuration configuration = headband.getConfiguration();
            configuration.getEegChannelLayout().stream()
                    .forEachOrdered(channel -> {
                        log.info("{}: {}", channel.getSensor(), headbandStatus.forChannel(channel));
                    });
        }
    }

    @Stream(path = HEADBAND_ON)
    public void on(Headband headband, SingleValue<Integer> headbandTouching)
            throws Exception {
        if (!this.headbandTouching.sameAs(headbandTouching)) {
            this.headbandTouching = headbandTouching;
            log.info("Touching: {}", headbandTouching.get());
        }
    }
}
