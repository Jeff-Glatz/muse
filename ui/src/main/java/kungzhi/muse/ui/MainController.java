package kungzhi.muse.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import kungzhi.muse.chart.XYChartAnimator;
import kungzhi.muse.model.BandPower;
import kungzhi.muse.model.Battery;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.EegChannel;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.HeadbandStatus;
import kungzhi.muse.model.HeadbandTouching;
import kungzhi.muse.model.Version;
import kungzhi.muse.osc.service.MessageClient;
import kungzhi.muse.osc.service.MessageDispatcher;
import kungzhi.muse.osc.service.MessagePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import static java.lang.String.format;
import static javafx.application.Platform.runLater;
import static kungzhi.muse.osc.service.MessagePath.ALPHA_RELATIVE;
import static kungzhi.muse.osc.service.MessagePath.BETA_RELATIVE;
import static kungzhi.muse.osc.service.MessagePath.DELTA_RELATIVE;
import static kungzhi.muse.osc.service.MessagePath.GAMMA_RELATIVE;
import static kungzhi.muse.osc.service.MessagePath.THETA_RELATIVE;

@Controller
public class MainController
        extends AbstractController {
    private static final String BATTERY_RED = "battery-red";
    private static final String BATTERY_YELLOW = "battery-yellow";
    private static final String BATTERY_ORANGE = "battery-orange";
    private static final String BATTERY_GREEN = "battery-green";
    private static final String[] BATTERY_CLASSES = {BATTERY_RED, BATTERY_YELLOW, BATTERY_ORANGE, BATTERY_GREEN};

    private static final String SENSOR_GOOD = "sensor-good";
    private static final String SENSOR_OK = "sensor-ok";
    private static final String SENSOR_BAD = "sensor-bad";
    private static final String[] SENSOR_CLASSES = {SENSOR_GOOD, SENSOR_OK, SENSOR_BAD};

    private final Map<EegChannel, RadioButton> sensorIndicatorMap = new HashMap<>();
    private final Headband headband;
    private final MessageClient client;
    private final MessageDispatcher dispatcher;
    private final XYChartAnimator<Number> animator;

    @FXML
    private HBox headbandStatusBox;

    @FXML
    private ProgressBar batteryProgressBar;

    @FXML
    private LineChart<Number, Number> bandPowerLineChart;

    @FXML
    private ToggleButton clientToggleButton;

    @FXML
    private TextField macAddress;

    @FXML
    private TextField serialNumber;

    @FXML
    private TextField preset;

    @FXML
    private TextField channelCount;

    @FXML
    private TextField hardwareVersion;

    @FXML
    private TextField firmwareHeadsetVersion;

    @Autowired
    public MainController(Clock clock, Headband headband,
                          MessageClient client, MessageDispatcher dispatcher) {
        this.headband = headband;
        this.client = client;
        this.dispatcher = dispatcher;
        this.animator = new XYChartAnimator<>(clock);
    }

    @PostConstruct
    public void registerHeadbandListeners() {
        Configuration configuration = headband.getConfiguration();
        configuration.addActiveItemListener((current, previous) -> {
            if (previous.initial()) {
                log.info("Muse configuration received: {}", current);
                runLater(() -> buildSensorStatusDisplay(current));
                animator.start();
            }
            runLater(this::updateDetailsView);
        });

        Battery battery = headband.getBattery();
        battery.addActiveItemListener((current, previous) -> runLater(() -> {
            Float remaining = current.getPercentRemaining();
            batteryProgressBar.setProgress(remaining / 100f);
            batteryProgressBar.getTooltip()
                    .setText(format("Battery: %.2f%%", remaining));
        }));

        HeadbandStatus status = headband.getStatus();
        status.addActiveItemListener((current, previous) -> runLater(() ->
                sensorIndicatorMap.forEach((channel, indicator) -> {
                    ObservableList<String> styles = indicator.getStyleClass();
                    styles.removeAll(SENSOR_CLASSES);
                    switch (current.forChannel(channel)) {
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
                })));

        HeadbandTouching touching = headband.getTouching();
        touching.addActiveItemListener(((current, previous) -> {
        }));
    }

    @Override
    protected void onInitialize() {
        animator.setChart(bandPowerLineChart);
        buildBandPowerChart();
        buildBatteryProgressBar();
    }

    @FXML
    public void toggleClient()
            throws IOException {
        if (clientToggleButton.isSelected()) {
            client.on();
            clientToggleButton.setText("On");
        } else {
            client.off();
            clientToggleButton.setText("Off");
        }
    }

    private void buildSensorStatusDisplay(Configuration configuration) {
        SortedSet<EegChannel> channels = configuration.getEegChannelLayout();
        channels.forEach(channel -> {
            RadioButton indicator = new RadioButton(channel.getName());
            indicator.setTooltip(new Tooltip(format("Sensor %s status", channel.getName())));
            indicator.setPadding(new Insets(0, 5, 0, 5));
            ObservableList<String> styles = indicator.getStyleClass();
            styles.add("sensor-indicator");
            sensorIndicatorMap.put(channel, indicator);
            headbandStatusBox.getChildren().add(indicator);
        });
    }

    private void buildBandPowerChart() {
        bandPowerLineChart.setTitle(localize("chart.bandPower.relative"));
        bandPowerLineChart.setAnimated(true);
        bandPowerLineChart.setVerticalGridLinesVisible(false);
        bandPowerLineChart.setCreateSymbols(false);

        NumberAxis xAxis = (NumberAxis) bandPowerLineChart.getXAxis();
        xAxis.setAutoRanging(false);
        xAxis.setAnimated(true);
        xAxis.setTickLabelsVisible(false);
        xAxis.setForceZeroInRange(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(30);
        xAxis.setTickUnit(5);

        NumberAxis yAxis = (NumberAxis) bandPowerLineChart.getYAxis();
        yAxis.setLabel(localize("chart.bandPower.axis.power"));
        yAxis.setAutoRanging(false);
        yAxis.setAnimated(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(1);
        yAxis.setTickUnit(0.2);

        ObservableList<Series<Number, Number>> seriesData = bandPowerLineChart.getData();
        seriesData.add(bandPowerSeries(GAMMA_RELATIVE, "band.gamma.name"));
        seriesData.add(bandPowerSeries(BETA_RELATIVE, "band.beta.name"));
        seriesData.add(bandPowerSeries(ALPHA_RELATIVE, "band.alpha.name"));
        seriesData.add(bandPowerSeries(THETA_RELATIVE, "band.theta.name"));
        seriesData.add(bandPowerSeries(DELTA_RELATIVE, "band.delta.name"));
    }

    private void buildBatteryProgressBar() {
        batteryProgressBar.setTooltip(new Tooltip("Battery"));
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
    }

    private void updateDetailsView() {
        Configuration configuration = headband.getConfiguration();
        macAddress.setText(configuration.getMacAddress());
        serialNumber.setText(configuration.getSerialNumber());
        preset.setText(configuration.getPreset().getId());
        channelCount.setText(format("%d", configuration.getEegChannelCount()));

        Version version = headband.getVersion();
        hardwareVersion.setText(version.getHardwareVersion());
        firmwareHeadsetVersion.setText(version.getFirmwareHeadsetVersion());
    }

    private Series<Number, Number> bandPowerSeries(MessagePath path, String labelKey) {
        Series<Number, Number> series = new Series<>();
        series.setName(localize(labelKey));
        dispatcher.withStream(path, BandPower.class,
                (headband, power) -> animator.offer(series, power.average()));
        return series;
    }
}
