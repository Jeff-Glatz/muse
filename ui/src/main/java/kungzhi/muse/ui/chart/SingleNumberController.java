package kungzhi.muse.ui.chart;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.SingleValue;
import kungzhi.muse.osc.service.MessageAddress;
import kungzhi.muse.osc.service.MessageDispatcher;
import kungzhi.muse.ui.common.AbstractController;
import kungzhi.ui.chart.realtime.XYChartAnimator;

import java.time.Clock;

public abstract class SingleNumberController
        extends AbstractController {
    private final XYChartAnimator<Number> animator;
    private final Headband headband;
    private final MessageDispatcher dispatcher;
    private final MessageAddress address;
    private final String resourceKey;

    @FXML
    protected LineChart<Number, Number> singleValueChart;

    protected SingleNumberController(Clock clock, Headband headband, MessageDispatcher dispatcher,
                                     MessageAddress address, String resourceKey) {
        this.animator = new XYChartAnimator<>(clock);
        this.headband = headband;
        this.dispatcher = dispatcher;
        this.address = address;
        this.resourceKey = resourceKey;
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
        animator.setChart(singleValueChart);

        ObservableList<Series<Number, Number>> seriesData = singleValueChart.getData();
        seriesData.add(singleValueSeries());

        Configuration configuration = headband.getConfiguration();
        configuration.addActiveItemListener((current, previous) -> {
            if (previous.initial()) {
                log.info("Muse configuration received: {}", current);
                animator.start();
            }
        });
    }

    private Series<Number, Number> singleValueSeries() {
        Series<Number, Number> series = new Series<>();
        series.setName(localize(resourceKey));
        dispatcher.withStream(address, SingleValue.class,
                (headband, value) -> animator.offer(series, value.getTime(), (Number) value.get()));
        return series;
    }
}
