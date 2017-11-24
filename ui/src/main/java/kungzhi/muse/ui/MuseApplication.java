package kungzhi.muse.ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import kungzhi.muse.model.BandPower;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.Model;
import kungzhi.muse.osc.service.MessageClient;
import kungzhi.muse.osc.service.MessageDispatcher;
import kungzhi.muse.osc.service.MessagePath;
import kungzhi.muse.runtime.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import static java.lang.System.currentTimeMillis;
import static java.util.ResourceBundle.getBundle;
import static kungzhi.muse.osc.service.MessagePath.ALPHA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.BETA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.DELTA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.GAMMA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.THETA_ABSOLUTE;

public class MuseApplication
        extends Application {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Queue<QueuedData<BandPower>> powers = new LinkedList<>();
    private final SpringContext context = new SpringContext(this);

    @Autowired
    private FXMLLoader loader;
    @Autowired
    private ExecutorService executor;
    @Autowired
    private MessageDispatcher dispatcher;
    @Autowired
    private MessageClient client;
    @Autowired
    private BatteryUi batteryUi;
    @Autowired
    private HeadbandStatusUi headbandStatusUi;

    private long start;

    @Override
    public void init()
            throws Exception {
        super.init();
        context.init();
        // Attach a listener to the configuration of the headband so that
        // we will be notified when it has been received. Data cannot be
        // analyzed until the configuration has been received because
        // it contains channel information and preset details needed to
        // interpret that data. All data received will be queued until the
        // configuration arrives, at which point the data will then be processed
        Headband headband = context.lookup(Headband.class);
        Configuration configuration = headband.getConfiguration();
        configuration.addActiveItemListener((current, previous) -> {
            if (previous.initial()) {
                log.info("Configuration received, processing queued data...");
                powers.stream().forEachOrdered(data -> addTo(data.series, data.model));
                log.info("Queued data processed.");
            }
        });
    }

    @Override
    public void start(Stage stage)
            throws Exception {
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Timestamp");
        xAxis.setAutoRanging(true);
        xAxis.setAnimated(false);
        xAxis.setForceZeroInRange(false);

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Absolute Power");
        yAxis.setAutoRanging(false);
        yAxis.setAnimated(false);
        yAxis.setLowerBound(-1);
        yAxis.setUpperBound(2);

        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Brainwave Monitoring");
        lineChart.setAnimated(true);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);

        ObservableList<XYChart.Series<Number, Number>> data = lineChart.getData();
        data.add(bandPowerSeries(GAMMA_ABSOLUTE));
        data.add(bandPowerSeries(BETA_ABSOLUTE));
        data.add(bandPowerSeries(ALPHA_ABSOLUTE));
        data.add(bandPowerSeries(THETA_ABSOLUTE));
        data.add(bandPowerSeries(DELTA_ABSOLUTE));

        // Start streaming messages
        start = currentTimeMillis();
        client.on();

        stage.setTitle("Muse EEG Feed");
        stage.setOnCloseRequest(event -> {
            context.close();
        });
        loader.setResources(getBundle("kungzhi.muse.ui.muse"));
        loader.setLocation(getClass().getResource("main.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setScene(new Scene(lineChart, 800, 600));
        stage.show();
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
                        powers.offer(new QueuedData<>(series, power));
                        return;
                    }
                    addTo(series, power);
                });
        return series;
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

    public static void main(String[] args) {
        launch(args);
    }

    private static class QueuedData<M extends Model> {
        final XYChart.Series<Number, Number> series;
        final M model;

        public QueuedData(XYChart.Series<Number, Number> series, M model) {
            this.series = series;
            this.model = model;
        }
    }
}
