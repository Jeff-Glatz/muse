package kungzhi.muse.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import kungzhi.muse.chart.XYChartData;
import kungzhi.muse.model.Band;
import kungzhi.muse.model.BandPower;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Headband;
import kungzhi.muse.osc.service.MessageDispatcher;
import kungzhi.muse.osc.service.MessagePath;
import kungzhi.muse.repository.Bands;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.Queue;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static javafx.animation.Animation.INDEFINITE;
import static javafx.util.Duration.millis;
import static kungzhi.muse.osc.service.MessagePath.valueOf;

public abstract class BandPowerChartController
        extends AbstractController {
    private final Queue<XYChartData<Number, Number>> chartDataQueue = new LinkedList<>();
    private final Timeline timeline = new Timeline();
    private final Bands bands;
    private final Headband headband;
    private final MessageDispatcher dispatcher;
    private final boolean relative;

    private long startedAt = 0;
    private int secondsOfHistory = 30;
    private int maxDataWindow = 1000;

    @FXML
    protected LineChart<Number, Number> bandPowerChart;

    protected BandPowerChartController(
            Bands bands, Headband headband,
            MessageDispatcher dispatcher, boolean relative) {
        this.bands = bands;
        this.headband = headband;
        this.dispatcher = dispatcher;
        this.relative = relative;
        this.timeline.getKeyFrames()
                .add(new KeyFrame(millis(1000 / 60), (event) -> {
                    for (int count = 0; count < 6; count++) {
                        addToSeries();
                    }
                }));
        this.timeline.setCycleCount(INDEFINITE);
    }

    public int getSecondsOfHistory() {
        return secondsOfHistory;
    }

    public void setSecondsOfHistory(int secondsOfHistory) {
        this.secondsOfHistory = secondsOfHistory;
    }

    public int getMaxDataWindow() {
        return maxDataWindow;
    }

    public void setMaxDataWindow(int maxDataWindow) {
        this.maxDataWindow = maxDataWindow;
    }

    @PostConstruct
    public void registerDomainListeners() {
        Configuration configuration = headband.getConfiguration();
        configuration.addActiveItemListener((current, previous) -> {
            if (previous.initial()) {
                log.info("Muse configuration received: {}", current);
                startedAt = currentTimeMillis();
                timeline.play();
            }
        });
    }

    @Override
    protected void initialize() {
        ObservableList<Series<Number, Number>> seriesData = bandPowerChart.getData();
        seriesData.add(bandPowerSeries(bands.load("gamma")));
        seriesData.add(bandPowerSeries(bands.load("beta")));
        seriesData.add(bandPowerSeries(bands.load("alpha")));
        seriesData.add(bandPowerSeries(bands.load("theta")));
        seriesData.add(bandPowerSeries(bands.load("delta")));
    }

    private double secondsSinceStart() {
        return (currentTimeMillis() - startedAt) / 1000D;
    }

    private void addToSeries() {
        for (XYChartData<Number, Number> data = chartDataQueue.poll();
             data != null;
             data = chartDataQueue.poll()) {
            data.addToSeries(maxDataWindow);
        }
        double elapsed = secondsSinceStart();
        if (elapsed > secondsOfHistory) {
            NumberAxis xAxis = (NumberAxis) bandPowerChart.getXAxis();
            xAxis.setUpperBound(elapsed);
            xAxis.setLowerBound(elapsed - secondsOfHistory);
        }
    }

    private void addToQueue(Series<Number, Number> series, BandPower power) {
        chartDataQueue.offer(new XYChartData<>(series,
                new XYChart.Data<>(secondsSinceStart(), power.average())));
    }

    private Series<Number, Number> bandPowerSeries(Band band) {
        MessagePath path = valueOf(format("%s_%s",
                band.getIdentifier().toUpperCase(),
                relative ? "RELATIVE" : "ABSOLUTE"));
        Series<Number, Number> series = new Series<>();
        series.setName(resources.getString(band.resourceKey()));
        dispatcher.withStream(path, BandPower.class,
                (headband, power) -> addToQueue(series, power));
        return series;
    }
}
