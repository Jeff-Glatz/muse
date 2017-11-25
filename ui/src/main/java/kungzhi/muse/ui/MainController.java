package kungzhi.muse.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import kungzhi.muse.chart.XYChartData;
import kungzhi.muse.model.BandPower;
import kungzhi.muse.model.Battery;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.EegChannel;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.HeadbandStatus;
import kungzhi.muse.model.HeadbandTouching;
import kungzhi.muse.osc.service.MessageClient;
import kungzhi.muse.osc.service.MessageDispatcher;
import kungzhi.muse.osc.service.MessagePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.SortedSet;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static javafx.animation.Animation.INDEFINITE;
import static javafx.application.Platform.runLater;
import static javafx.util.Duration.millis;
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
    private final Queue<XYChartData<Number, Number>> powers = new LinkedList<>();
    private final int secondsOfHistory = 30;
    private final Headband headband;
    private final MessageClient client;
    private final MessageDispatcher dispatcher;
    private final Timeline timeline;

    private ResourceBundle resources;
    private long start;

    @FXML
    private HBox headbandStatusBox;

    @FXML
    private ProgressBar batteryProgressBar;

    @FXML
    private LineChart<Number, Number> bandPowerLineChart;

    @FXML
    private ToggleButton clientToggleButton;

    @Autowired
    public MainController(Headband headband, MessageClient client, MessageDispatcher dispatcher) {
        this.headband = headband;
        this.client = client;
        this.dispatcher = dispatcher;
        this.timeline = new Timeline();
        this.timeline.getKeyFrames()
                .add(new KeyFrame(millis(1000 / 60), (event) -> {
                    for (int count = 0; count < 6; count++) {
                        addToSeries();
                    }
                }));
        this.timeline.setCycleCount(INDEFINITE);
    }

    @PostConstruct
    public void registerHeadbandListeners() {
        Configuration configuration = headband.getConfiguration();
        configuration.addActiveItemListener((current, previous) -> {
            if (previous.initial()) {
                log.info("Muse configuration received: {}", current);
                runLater(() -> {
                    buildSensorStatusDisplay(current);
                });
                timeline.play();
            }
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
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Initializing controller from {}", location);
        this.resources = resources;
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
        buildBandPowerChart();
        start = currentTimeMillis();
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

    private double secondsSinceStart() {
        return (currentTimeMillis() - start) / 1000D;
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

    private void addToSeries() {
        for (XYChartData<Number, Number> data = powers.poll();
             data != null;
             data = powers.poll()) {
            data.addToSeries(1000);
        }
        double elapsed = secondsSinceStart();
        if (elapsed > secondsOfHistory) {
            NumberAxis xAxis = (NumberAxis) bandPowerLineChart.getXAxis();
            xAxis.setUpperBound(elapsed);
            xAxis.setLowerBound(elapsed - secondsOfHistory);
        }
    }

    private void addToQueue(Series<Number, Number> series, BandPower power) {
        powers.offer(new XYChartData<>(series,
                new XYChart.Data<>(secondsSinceStart(), power.average())));
    }

    /**
     * Attaches a stream to the message dispatcher for consuming band power
     * data. Any data received on the specified message path will be queued
     * until the headband's configuration arrives.
     *
     * @param path The path containg the band power data to be plotted
     * @return A new series for the specified band
     */
    private Series<Number, Number> bandPowerSeries(MessagePath path, String labelKey) {
        Series<Number, Number> series = new Series<>();
        series.setName(resources.getString(labelKey));
        dispatcher.withStream(path, BandPower.class,
                (headband, power) -> addToQueue(series, power));
        return series;
    }

    private void buildBandPowerChart() {
        bandPowerLineChart.setTitle("Relative Band Power");
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
        yAxis.setLabel("Power");
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
}
