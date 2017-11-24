package kungzhi.muse.ui;

import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.HeadbandStatus;
import kungzhi.muse.model.HeadbandStatusStrict;
import kungzhi.muse.model.SingleValue;
import kungzhi.muse.osc.service.Stream;
import kungzhi.muse.osc.service.StreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import static kungzhi.muse.osc.service.MessagePath.HEADBAND_ON;
import static kungzhi.muse.osc.service.MessagePath.HEADBAND_STATUS;
import static kungzhi.muse.osc.service.MessagePath.HEADBAND_STATUS_STRICT;

@Stream
@Controller
public class HeadbandController
        extends AbstractController {
    private final Headband headband;

    private HeadbandStatus status = new HeadbandStatus(0, null);
    private SingleValue<Integer> touching = new SingleValue<>(0, null);

    @Autowired
    public HeadbandController(Headband headband) {
        this.headband = headband;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Initializing controller from {}", location);
    }

    @StreamHandler(HEADBAND_STATUS)
    public void on(Headband headband, HeadbandStatus headbandStatus)
            throws Exception {
        log.info(headbandStatus.toString());
        if (!this.status.sameAs(headbandStatus)) {
            this.status = headbandStatus;
            Configuration configuration = headband.getConfiguration();
            configuration.getEegChannelLayout().stream()
                    .forEachOrdered(channel -> {
                        log.info("{}: {}", channel.getSensor(), headbandStatus.forChannel(channel));
                    });
        }
    }

    @StreamHandler(HEADBAND_STATUS_STRICT)
    public void on(Headband headband, HeadbandStatusStrict headbandStatus)
            throws Exception {
    }

    @StreamHandler(HEADBAND_ON)
    public void on(Headband headband, SingleValue<Integer> headbandTouching)
            throws Exception {
        if (!this.touching.sameAs(headbandTouching)) {
            this.touching = headbandTouching;
            log.info("Touching: {}", headbandTouching.get());
        }
    }
}
