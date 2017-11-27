package kungzhi.muse.chart;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XYChartData<X, Y> {
    private static final Logger log = LoggerFactory.getLogger(XYChartData.class);
    public final Series<X, Y> series;
    public final Data<X, Y> data;

    public XYChartData(Series<X, Y> series, Data<X, Y> data) {
        this.series = series;
        this.data = data;
    }

    public void addToSeries(double window) {
        ObservableList<Data<X, Y>> list = series.getData();
        list.add(data);
        if (list.size() > window) {
            list.remove(0);
        }
        log.info("added {} to {}", data.getYValue(), series.getName());
    }
}
