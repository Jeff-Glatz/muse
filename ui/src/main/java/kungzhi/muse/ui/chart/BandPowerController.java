package kungzhi.muse.ui.chart;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import kungzhi.ui.chart.XYChartAnimator;
import kungzhi.muse.model.Band;
import kungzhi.muse.model.BandPower;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Headband;
import kungzhi.muse.osc.service.MessageDispatcher;
import kungzhi.muse.osc.service.MessageAddress;
import kungzhi.muse.repository.Bands;
import kungzhi.muse.ui.common.AbstractController;

import java.time.Clock;

import static java.lang.String.format;
import static kungzhi.muse.osc.service.MessageAddress.valueOf;

public abstract class BandPowerController
        extends AbstractController {
    private final XYChartAnimator<Number> animator;
    private final Bands bands;
    private final Headband headband;
    private final MessageDispatcher dispatcher;
    private final boolean relative;

    @FXML
    protected LineChart<Number, Number> bandPowerChart;

    protected BandPowerController(Clock clock, Bands bands, Headband headband,
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
    protected void onInitialize() {
        animator.setChart(bandPowerChart);

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
        MessageAddress address = valueOf(format("%s_%s",
                band.getIdentifier().toUpperCase(),
                relative ? "RELATIVE" : "ABSOLUTE"));
        log.info("Streaming data on {} for {}", address, band.getIdentifier());
        Series<Number, Number> series = new Series<>();
        series.setName(localize(band));
        dispatcher.withStream(address, BandPower.class,
                (headband, power) -> animator.offer(series, power.average()));
        return series;
    }
}
