package kungzhi.ui.chart.realtime;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.LinkedList;
import java.util.Queue;

import static javafx.animation.Animation.INDEFINITE;
import static javafx.util.Duration.millis;

public class XYChartAnimator<Y> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Queue<XYChartData<Number, Y>> queue = new LinkedList<>();
    private final Clock clock;
    private final Timeline timeline;

    private LineChart<Number, Y> chart;
    private int secondsOfHistory = 30;
    private int maxDataWindow = 1000;

    public XYChartAnimator(Clock clock) {
        this.clock = clock;
        this.timeline = new Timeline();
        this.timeline.getKeyFrames()
                .add(new KeyFrame(millis(1000 / 60), (event) ->
                        addQueuedDataToChart()));
        this.timeline.setCycleCount(INDEFINITE);
    }

    public LineChart<Number, Y> getChart() {
        return chart;
    }

    public void setChart(LineChart<Number, Y> chart) {
        this.chart = chart;
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

    public void start() {
        if (chart == null) {
            throw new IllegalStateException("Missing chart to animate");
        }
        timeline.play();
    }

    public boolean offer(Series<Number, Y> series, Number x, Y y) {
        return queue.offer(new XYChartData<>(series,
                new XYChart.Data<>(x, y)));
    }

    public void stop() {
        timeline.stop();
    }

    private void addQueuedDataToChart() {
        for (XYChartData<Number, Y> data = queue.poll();
             data != null;
             data = queue.poll()) {
            data.addToSeries(maxDataWindow);
        }
        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        double upperBound = clock.millis();
        xAxis.setUpperBound(upperBound);
        xAxis.setLowerBound(upperBound - millisecondsOfHistory());
    }

    private long millisecondsOfHistory() {
        return secondsOfHistory * 1000;
    }
}
