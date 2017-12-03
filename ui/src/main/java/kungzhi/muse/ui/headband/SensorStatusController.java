package kungzhi.muse.ui.headband;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.EegChannel;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.HeadbandStatus;
import kungzhi.muse.ui.common.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import static javafx.application.Platform.runLater;

@Controller
public class SensorStatusController
        extends AbstractController {
    private static final String SENSOR_GOOD = "sensor-good";
    private static final String SENSOR_OK = "sensor-ok";
    private static final String SENSOR_BAD = "sensor-bad";
    private static final String[] SENSOR_CLASSES = {SENSOR_GOOD, SENSOR_OK, SENSOR_BAD};

    private final Map<EegChannel, RadioButton> sensorIndicatorMap = new HashMap<>();
    private final Headband headband;

    @FXML
    private HBox headbandStatusBox;

    @Autowired
    public SensorStatusController(Headband headband) {
        this.headband = headband;
    }

    @Override
    protected void onInitialize() {
        Configuration configuration = headband.getConfiguration();
        configuration.addActiveItemListener((current, previous) -> {
            if (previous.initial()) {
                runLater(() -> buildSensorStatusDisplay(current));
            } else {
                if (previous.eegChannelLayoutChanged(current)) {
                    log.info("Sensor Layout changed, rebuilding sensor display");
                    runLater(() -> buildSensorStatusDisplay(current));
                }
            }
        });

        HeadbandStatus status = headband.getStatus();
        status.addActiveItemListener((current, previous) -> runLater(() ->
                updateIndicators(current)));
    }

    private void buildSensorStatusDisplay(Configuration configuration) {
        log.info("Muse configuration received, building sensor status display");
        ObservableList<Node> children = headbandStatusBox.getChildren();
        children.clear();
        sensorIndicatorMap.clear();
        SortedSet<EegChannel> channels = configuration.getEegChannelLayout();
        channels.forEach(channel -> {
            RadioButton indicator = new RadioButton(channel.getName());
            indicator.setTooltip(new Tooltip(localize("model.sensor.status", channel.getName())));
            indicator.setPadding(new Insets(0, 5, 0, 5));
            ObservableList<String> styles = indicator.getStyleClass();
            styles.add("sensor-indicator");
            sensorIndicatorMap.put(channel, indicator);
            children.add(indicator);
        });
    }

    private void updateIndicators(HeadbandStatus status) {
        sensorIndicatorMap.forEach((channel, indicator) -> {
            ObservableList<String> styles = indicator.getStyleClass();
            styles.removeAll(SENSOR_CLASSES);
            switch (status.forChannel(channel)) {
                case GOOD:
                    indicator.setSelected(true);
                    styles.add(SENSOR_GOOD);
                    break;
                case OK:
                    indicator.setSelected(true);
                    styles.add(SENSOR_OK);
                    break;
                case BAD:
                    indicator.setSelected(true);
                    styles.add(SENSOR_BAD);
                    break;
            }
        });
    }
}
