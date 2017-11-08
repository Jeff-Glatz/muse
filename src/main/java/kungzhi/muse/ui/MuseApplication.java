package kungzhi.muse.ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import kungzhi.muse.config.MuseConfiguration;
import kungzhi.muse.model.BandPower;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Session;
import kungzhi.muse.model.SessionListener;
import kungzhi.muse.osc.MessageDispatcher;
import kungzhi.muse.osc.MessageReceiver;
import kungzhi.muse.osc.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ExecutorService;

import static java.lang.System.currentTimeMillis;
import static kungzhi.muse.osc.Path.ALPHA_ABSOLUTE;
import static kungzhi.muse.osc.Path.BETA_ABSOLUTE;
import static kungzhi.muse.osc.Path.DELTA_ABSOLUTE;
import static kungzhi.muse.osc.Path.GAMMA_ABSOLUTE;
import static kungzhi.muse.osc.Path.THETA_ABSOLUTE;
import static kungzhi.muse.ui.AsyncModelStream.asynchronously;

public class MuseApplication
        extends Application
        implements SessionListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private long start;
    private ConfigurableApplicationContext context;
    private Session session;
    private ExecutorService executor;
    private MessageDispatcher dispatcher;
    private MessageReceiver receiver;
    private Configuration configuration;

    @Override
    public void configurationChanged(Configuration previous, Configuration current) {
        this.configuration = current;
    }

    @Override
    public void init()
            throws Exception {
        super.init();
        start = currentTimeMillis();
        context = new SpringApplicationBuilder(MuseConfiguration.class)
                .headless(false)
                .registerShutdownHook(true)
                .run(getParameters().getRaw().toArray(new String[0]));
        session = context.getBean(Session.class);
        session.addSessionListener((previous, current) -> {

        });
        executor = context.getBean(ExecutorService.class);
        dispatcher = context.getBean(MessageDispatcher.class);
        receiver = context.getBean(MessageReceiver.class)
                .withProtocol("udp")
                .onAddress(5000);
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
        receiver.on();

        stage.setScene(scene);
        stage.show();
    }

    private XYChart.Series<Number, Number> bandPowerSeries(Path path) {
        XYChart.Series<Number, Number> band = new XYChart.Series<>();
        band.setName(path.name());
        dispatcher.withStream(path, BandPower.class,
                asynchronously(executor, (session, model) -> {
                    if (configuration == null) {
                        return;
                    }
                    double seconds = (currentTimeMillis() - start) / 1000D;
                    double average = model.average();
                    ObservableList<XYChart.Data<Number, Number>> data = band.getData();
                    data.add(new XYChart.Data<>(seconds, average));
                    while (data.size() > 100) {
                        data.remove(0);
                    }
                }));
        return band;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
