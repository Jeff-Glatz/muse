package kungzhi.muse.ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import kungzhi.muse.config.MuseConfiguration;
import kungzhi.muse.model.Band;
import kungzhi.muse.model.BandPower;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.EegChannel;
import kungzhi.muse.model.Model;
import kungzhi.muse.osc.MessageClient;
import kungzhi.muse.osc.MessageDispatcher;
import kungzhi.muse.osc.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedSet;
import java.util.concurrent.ExecutorService;

import static java.lang.System.currentTimeMillis;
import static kungzhi.muse.osc.Path.ALPHA_ABSOLUTE;
import static kungzhi.muse.osc.Path.BETA_ABSOLUTE;
import static kungzhi.muse.osc.Path.DELTA_ABSOLUTE;
import static kungzhi.muse.osc.Path.GAMMA_ABSOLUTE;
import static kungzhi.muse.osc.Path.THETA_ABSOLUTE;
import static kungzhi.muse.ui.AsyncModelStream.asynchronously;

public class MuseApplication
        extends Application {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Queue<QueuedData<BandPower>> powers = new LinkedList<>();
    private long start;
    private ConfigurableApplicationContext context;
    private ExecutorService executor;
    private MessageDispatcher dispatcher;
    private MessageClient client;
    private Configuration configuration;

    @Override
    public void init()
            throws Exception {
        super.init();
        start = currentTimeMillis();
        context = new SpringApplicationBuilder(MuseConfiguration.class)
                .headless(false)
                .registerShutdownHook(true)
                .properties(new HashMap<>(getParameters().getNamed()))
                .run(getParameters().getRaw().toArray(new String[0]));
        context.getBean(Configuration.class)
                .addActiveItemListener((previous, current) -> initialize(current));
        executor = context.getBean(ExecutorService.class);
        dispatcher = context.getBean(MessageDispatcher.class);
        client = context.getBean(MessageClient.class);
    }

    @Override
    public void start(Stage stage)
            throws Exception {
        stage.setTitle("Muse EEG Feed");
        stage.setOnCloseRequest(event -> {
        });

        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Timestamp");
        xAxis.setAutoRanging(true);
        xAxis.setAnimated(true);
        xAxis.setForceZeroInRange(false);

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Absolute Power");
        yAxis.setAutoRanging(false);
        yAxis.setAnimated(true);
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

    private void initialize(Configuration current) {
        if (configuration == null) {
            configuration = current;
            log.info("Configuration received, processing queued data...");
            powers.stream().forEachOrdered(data -> addTo(data.series, data.model));
            log.info("Queued data processed.");
        }
    }

    private XYChart.Series<Number, Number> bandPowerSeries(Path path) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(path.name());
        dispatcher.withStream(path, BandPower.class,
                asynchronously(executor, (session, power) -> {
                    if (configuration == null) {
                        log.warn("Configuration not yet received, queueing data");
                        powers.offer(new QueuedData<>(series, power));
                        return;
                    }
                    addTo(series, power);
                }));
        return series;
    }

    private void addTo(XYChart.Series<Number, Number> series, BandPower power) {
        double seconds = (currentTimeMillis() - start) / 1000D;
        SortedSet<EegChannel> channels = configuration.getEegChannelLayout();
        Band band = power.getBand();
        double average = power.average();
        ObservableList<XYChart.Data<Number, Number>> data = series.getData();
        data.add(new XYChart.Data<>(seconds, average));
        while (data.size() > 100) {
            data.remove(0);
        }
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
