package kungzhi.muse.ui;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import kungzhi.muse.model.BandPower;
import kungzhi.muse.model.Battery;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.EegChannel;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.HeadbandStatus;
import kungzhi.muse.model.HeadbandTouching;
import kungzhi.muse.model.XYChartData;
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
import java.util.concurrent.ExecutorService;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static javafx.application.Platform.runLater;
import static kungzhi.muse.osc.service.MessagePath.ALPHA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.BETA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.DELTA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.GAMMA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.THETA_ABSOLUTE;

@Controller
public class MainController
        extends AbstractController {
    private static final String BATTERY_RED = "battery-red";
    private static final String BATTERY_YELLOW = "battery-yellow";
    private static final String BATTERY_ORANGE = "battery-orange";
    private static final String BATTERY_GREEN = "battery-green";
    private static final String[] barColorStyleClasses = {BATTERY_RED, BATTERY_YELLOW, BATTERY_ORANGE, BATTERY_GREEN};

    private final Map<EegChannel, CheckBox> sensors = new HashMap<>();
    private final Queue<XYChartData<Number, Number, BandPower>> powers = new LinkedList<>();
    private final ExecutorService executor;
    private final Headband headband;
    private final MessageClient client;
    private final MessageDispatcher dispatcher;

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
    public MainController(ExecutorService executor, Headband headband,
                          MessageClient client, MessageDispatcher dispatcher) {
        this.executor = executor;
        this.headband = headband;
        this.client = client;
        this.dispatcher = dispatcher;
    }

    @PostConstruct
    public void registerHeadbandListeners() {
        Configuration configuration = headband.getConfiguration();
        configuration.addActiveItemListener((current, previous) -> {
            if (previous.initial()) {
                buildHeadbandStatusDisplay(current);
                powers.forEach(data -> addTo(data.series, data.model));
            }
        });

        Battery battery = headband.getBattery();
        battery.addActiveItemListener((current, previous) -> {
            Float remaining = current.getPercentRemaining();
            batteryProgressBar.setProgress(remaining / 100f);
            batteryProgressBar.getTooltip()
                    .setText(format("Battery: %.2f%%", remaining));
        });

        HeadbandStatus status = headband.getStatus();
        status.addActiveItemListener((current, previous) -> {
            sensors.forEach((channel, checkBox) -> {
                switch (current.forChannel(channel)) {
                    case GOOD:
                        checkBox.setSelected(true);
                        break;
                    case OK:
                        checkBox.setIndeterminate(true);
                        break;
                    case BAD:
                        checkBox.setSelected(false);
                        break;
                }
            });
        });

        HeadbandTouching touching = headband.getTouching();
        touching.addActiveItemListener(((current, previous) -> {
        }));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Initializing controller from {}", location);
        batteryProgressBar.setTooltip(new Tooltip("Battery"));
        batteryProgressBar.progressProperty()
                .addListener((observable, oldValue, newValue) -> {
                    double progress = newValue == null ? 0 : newValue.doubleValue();
                    ObservableList<String> styleClass = batteryProgressBar.getStyleClass();
                    styleClass.removeAll(barColorStyleClasses);
                    if (progress < 0.2) {
                        styleClass.add(BATTERY_RED);
                    } else if (progress < 0.4) {
                        styleClass.add(BATTERY_ORANGE);
                    } else if (progress < 0.6) {
                        styleClass.add(BATTERY_YELLOW);
                    } else {
                        styleClass.add(BATTERY_GREEN);
                    }
                });
        buildAbsoluteBandPowerChart();
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

    private void buildHeadbandStatusDisplay(Configuration configuration) {
        SortedSet<EegChannel> channels = configuration.getEegChannelLayout();
        channels.forEach(channel -> {
            Label label = new Label(channel.getName());
            CheckBox checkBox = new CheckBox();
            checkBox.setTooltip(new Tooltip(channel.getName()));
            sensors.put(channel, checkBox);
            runLater(() -> {
                ObservableList<Node> children = headbandStatusBox.getChildren();
                children.add(label);
                children.add(checkBox);
            });
        });
    }

    /**
     * Submits a task to asynchronously add the data to the specified series
     *
     * @param series The series to which the incoming data will be added
     * @param power  The BandPower to add
     */
    private void addTo(XYChart.Series<Number, Number> series, BandPower power) {
        executor.submit(new Task<Double>() {
            @Override
            protected Double call()
                    throws Exception {
                return power.average();
            }

            @Override
            protected void succeeded() {
                try {
                    double seconds = (currentTimeMillis() - start) / 1000D;
                    ObservableList<XYChart.Data<Number, Number>> data = series.getData();
                    data.add(new XYChart.Data<>(seconds, get()));
                    while (data.size() > 100) {
                        data.remove(0);
                    }
                } catch (Exception e) {
                    log.error("Failure getting computed value", e);
                }
            }
        });
    }

    /**
     * Attaches a stream to the message dispatcher for consuming band power
     * data. Any data received on the specified message path will be queued
     * until the headband's configuration arrives.
     *
     * @param path The path containg the band power data to be plotted
     * @return A new series for the specified band
     */
    private XYChart.Series<Number, Number> bandPowerSeries(MessagePath path) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(path.name());
        dispatcher.withStream(path, BandPower.class,
                (headband, power) -> {
                    if (!headband.ready()) {
                        log.warn("Configuration not yet received, queueing data");
                        powers.offer(new XYChartData<>(series, power));
                        return;
                    }
                    addTo(series, power);
                });
        return series;
    }

    private void buildAbsoluteBandPowerChart() {
        bandPowerLineChart.setTitle("Absolute Band Power");
        bandPowerLineChart.setAnimated(true);
        bandPowerLineChart.setVerticalGridLinesVisible(false);
        bandPowerLineChart.setCreateSymbols(false);

        NumberAxis xAxis = (NumberAxis) bandPowerLineChart.getXAxis();
        xAxis.setLabel("Timestamp");
        xAxis.setAutoRanging(true);
        xAxis.setAnimated(false);
        xAxis.setForceZeroInRange(false);

        NumberAxis yAxis = (NumberAxis) bandPowerLineChart.getYAxis();
        yAxis.setLabel("Absolute Power");
        yAxis.setAutoRanging(false);
        yAxis.setAnimated(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(2);

        ObservableList<XYChart.Series<Number, Number>> seriesData = bandPowerLineChart.getData();
        seriesData.add(bandPowerSeries(GAMMA_ABSOLUTE));
        seriesData.add(bandPowerSeries(BETA_ABSOLUTE));
        seriesData.add(bandPowerSeries(ALPHA_ABSOLUTE));
        seriesData.add(bandPowerSeries(THETA_ABSOLUTE));
        seriesData.add(bandPowerSeries(DELTA_ABSOLUTE));
    }
}
