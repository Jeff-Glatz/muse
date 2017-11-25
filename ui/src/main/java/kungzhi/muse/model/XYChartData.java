package kungzhi.muse.model;

import javafx.scene.chart.XYChart;

public class XYChartData<X, Y, M extends Model> {
    public final XYChart.Series<X, Y> series;
    public final M model;

    public XYChartData(XYChart.Series<X, Y> series, M model) {
        this.series = series;
        this.model = model;
    }
}
