package kungzhi.muse.ui;

import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.HeadbandStatus;
import kungzhi.muse.model.SingleValue;
import kungzhi.muse.osc.service.Stream;
import kungzhi.muse.osc.service.StreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import static kungzhi.muse.osc.service.MessagePath.HEADBAND_ON;
import static kungzhi.muse.osc.service.MessagePath.HEADBAND_STATUS;

@Stream
@Controller
public class HeadbandController
        extends AbstractController {
    private final Headband headband;

    private HeadbandStatus headbandStatus = new HeadbandStatus(0, null);
    private SingleValue<Integer> headbandTouching = new SingleValue<>(0, null);

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
        if (!this.headbandStatus.sameAs(headbandStatus)) {
            this.headbandStatus = headbandStatus;
            Configuration configuration = headband.getConfiguration();
            configuration.getEegChannelLayout().stream()
                    .forEachOrdered(channel -> {
                        log.info("{}: {}", channel.getSensor(), headbandStatus.forChannel(channel));
                    });
        }
    }

    @StreamHandler(HEADBAND_ON)
    public void on(Headband headband, SingleValue<Integer> headbandTouching)
            throws Exception {
        if (!this.headbandTouching.sameAs(headbandTouching)) {
            this.headbandTouching = headbandTouching;
            log.info("Touching: {}", headbandTouching.get());
        }
    }
}
