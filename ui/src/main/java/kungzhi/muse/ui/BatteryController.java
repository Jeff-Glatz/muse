package kungzhi.muse.ui;

import kungzhi.muse.model.Battery;
import kungzhi.muse.model.Headband;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class BatteryController
        extends AbstractController {
    private final Headband headband;

    @Autowired
    public BatteryController(Headband headband) {
        this.headband = headband;
    }

    @PostConstruct
    public void addBatteryListener() {
        Battery battery = headband.getBattery();
        battery.addActiveItemListener((current, previous) -> {
            updateView();
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Initializing controller from {}", location);
    }

    private void updateView() {
        Battery battery = headband.getBattery();
        log.info("Battery: {}%", battery.getPercentRemaining());
    }
}
