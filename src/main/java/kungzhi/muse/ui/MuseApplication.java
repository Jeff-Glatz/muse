package kungzhi.muse.ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import kungzhi.muse.runtime.MuseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import static java.lang.System.currentTimeMillis;
import static kungzhi.muse.osc.service.MessagePath.ALPHA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.BETA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.DELTA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.GAMMA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.THETA_ABSOLUTE;

public class MuseApplication
        extends Application {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Queue<QueuedData<BandPower>> powers = new LinkedList<>();
    private long start;
    private ConfigurableApplicationContext context;
    private ExecutorService executor;
    private MessageDispatcher dispatcher;
    private MessageClient client;
    private Headband headband;

    @Override
    public void init()
            throws Exception {
        super.init();
        start = currentTimeMillis();
        context = new SpringApplicationBuilder(MuseConfiguration.class)
                .headless(false)
                .registerShutdownHook(true)
                .initializers(context -> context.getBeanFactory()
                        .registerSingleton("application", MuseApplication.this))
                .properties(new HashMap<>(getParameters().getNamed()))
                .run(getParameters().getRaw().toArray(new String[0]));
        executor = context.getBean(ExecutorService.class);
        dispatcher = context.getBean(MessageDispatcher.class);
        client = context.getBean(MessageClient.class);
        headband = context.getBean(Headband.class);
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
        stage.setTitle("Muse EEG Feed");
        stage.setOnCloseRequest(event -> {
            context.close();
        });

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

        Scene scene = new Scene(lineChart, 800, 600);
        ObservableList<XYChart.Series<Number, Number>> data = lineChart.getData();
        data.add(bandPowerSeries(GAMMA_ABSOLUTE));
        data.add(bandPowerSeries(BETA_ABSOLUTE));
        data.add(bandPowerSeries(ALPHA_ABSOLUTE));
        data.add(bandPowerSeries(THETA_ABSOLUTE));
        data.add(bandPowerSeries(DELTA_ABSOLUTE));

        // Start streaming messages
        client.on();

        stage.setScene(scene);
        stage.show();
    }

    private XYChart.Series<Number, Number> bandPowerSeries(MessagePath path) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(path.name());
        dispatcher.withStream(path, BandPower.class,
                (session, power) -> {
                    Configuration configuration = headband.getConfiguration();
                    if (configuration.initial()) {
                        log.warn("Configuration not yet received, queueing data");
                        powers.offer(new QueuedData<>(series, power));
                        return;
                    }
                    addTo(series, power);

                });
        return series;
    }

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
