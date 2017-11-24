package kungzhi.muse.ui;

import javafx.scene.chart.XYChart;
import kungzhi.muse.model.Model;

public class QueuedData<M extends Model> {
    final XYChart.Series<Number, Number> series;
    final M model;

    public QueuedData(XYChart.Series<Number, Number> series, M model) {
        this.series = series;
        this.model = model;
    }
}
