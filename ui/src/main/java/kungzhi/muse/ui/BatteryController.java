package kungzhi.muse.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import kungzhi.muse.model.Battery;
import kungzhi.muse.model.Headband;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static java.lang.String.format;
import static javafx.application.Platform.runLater;

@Controller
public class BatteryController
        extends AbstractController {
    private static final String BATTERY_RED = "battery-red";
    private static final String BATTERY_YELLOW = "battery-yellow";
    private static final String BATTERY_ORANGE = "battery-orange";
    private static final String BATTERY_GREEN = "battery-green";
    private static final String[] BATTERY_CLASSES = {BATTERY_RED, BATTERY_YELLOW, BATTERY_ORANGE, BATTERY_GREEN};

    private final Headband headband;

    @FXML
    private ProgressBar batteryProgressBar;

    @Autowired
    public BatteryController(Headband headband) {
        this.headband = headband;
    }

    @Override
    protected void onInitialize() {
        final String key = "tooltip.text.battery";
        batteryProgressBar.setTooltip(new Tooltip(localize(key)));
        batteryProgressBar.progressProperty()
                .addListener((observable, oldValue, newValue) -> {
                    double progress = newValue == null ? 0 : newValue.doubleValue();
                    ObservableList<String> styles = batteryProgressBar.getStyleClass();
                    styles.removeAll(BATTERY_CLASSES);
                    if (progress < 0.2) {
                        styles.add(BATTERY_RED);
                    } else if (progress < 0.4) {
                        styles.add(BATTERY_ORANGE);
                    } else if (progress < 0.6) {
                        styles.add(BATTERY_YELLOW);
                    } else {
                        styles.add(BATTERY_GREEN);
                    }
                });

        Battery battery = headband.getBattery();
        battery.addActiveItemListener((current, previous) -> runLater(() -> {
            Float remaining = current.getPercentRemaining();
            batteryProgressBar.setProgress(remaining / 100f);
            batteryProgressBar.getTooltip()
                    .setText(format("%s: %.2f%%", localize(key), remaining));
        }));
    }
}
