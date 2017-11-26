package kungzhi.muse.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import kungzhi.muse.chart.XYChartAnimator;
import kungzhi.muse.model.Band;
import kungzhi.muse.model.BandPower;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Headband;
import kungzhi.muse.osc.service.MessageDispatcher;
import kungzhi.muse.osc.service.MessagePath;
import kungzhi.muse.repository.Bands;

import java.time.Clock;

import static java.lang.String.format;
import static kungzhi.muse.osc.service.MessagePath.valueOf;

public abstract class BandPowerChartController
        extends AbstractController {
    private final XYChartAnimator<Number> animator;
    private final Bands bands;
    private final Headband headband;
    private final MessageDispatcher dispatcher;
    private final boolean relative;

    @FXML
    protected LineChart<Number, Number> bandPowerChart;

    protected BandPowerChartController(Clock clock, Bands bands, Headband headband,
                                       MessageDispatcher dispatcher, boolean relative) {
        this.animator = new XYChartAnimator<>(clock);
        this.bands = bands;
        this.headband = headband;
        this.dispatcher = dispatcher;
        this.relative = relative;
    }

    public int getSecondsOfHistory() {
        return animator.getSecondsOfHistory();
    }

    public void setSecondsOfHistory(int secondsOfHistory) {
        animator.setSecondsOfHistory(secondsOfHistory);
    }

    public int getMaxDataWindow() {
        return animator.getMaxDataWindow();
    }

    public void setMaxDataWindow(int maxDataWindow) {
        animator.setMaxDataWindow(maxDataWindow);
    }

    @Override
    protected void initialize() {
        ObservableList<Series<Number, Number>> seriesData = bandPowerChart.getData();
        seriesData.add(bandPowerSeries(bands.load("gamma")));
        seriesData.add(bandPowerSeries(bands.load("beta")));
        seriesData.add(bandPowerSeries(bands.load("alpha")));
        seriesData.add(bandPowerSeries(bands.load("theta")));
        seriesData.add(bandPowerSeries(bands.load("delta")));

        Configuration configuration = headband.getConfiguration();
        configuration.addActiveItemListener((current, previous) -> {
            if (previous.initial()) {
                log.info("Muse configuration received: {}", current);
                animator.start();
            }
        });
    }

    private Series<Number, Number> bandPowerSeries(Band band) {
        MessagePath path = valueOf(format("%s_%s",
                band.getIdentifier().toUpperCase(),
                relative ? "RELATIVE" : "ABSOLUTE"));
        Series<Number, Number> series = new Series<>();
        series.setName(resources.getString(band.resourceKey()));
        dispatcher.withStream(path, BandPower.class,
                (headband, power) -> animator.offer(series, power.average()));
        return series;
    }
}
