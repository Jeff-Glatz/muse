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
import kungzhi.muse.osc.MessageDispatcher;
import kungzhi.muse.osc.MessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ExecutorService;

import static java.lang.System.currentTimeMillis;
import static kungzhi.muse.osc.Path.THETA_ABSOLUTE;
import static kungzhi.muse.ui.AsyncModelStream.inBackground;

public class MuseApplication
        extends Application {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private ConfigurableApplicationContext context;

    @Override
    public void init()
            throws Exception {
        super.init();
        context = new SpringApplicationBuilder(MuseConfiguration.class)
                .headless(false)
                .registerShutdownHook(true)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage stage)
            throws Exception {
        ExecutorService executorService = context.getBean(ExecutorService.class);
        MessageDispatcher dispatcher = context.getBean(MessageDispatcher.class);
        MessageReceiver receiver = context.getBean(MessageReceiver.class)
                .withProtocol("udp")
                .onAddress(5000);

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
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(1);
        yAxis.setAnimated(true);

        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Brainwave Monitoring");
        lineChart.setAnimated(true);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);

        //defining a series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Theta Absolute");
        long start = System.currentTimeMillis();
        dispatcher.withStream(THETA_ABSOLUTE, BandPower.class,
                inBackground(executorService, (session, model) -> {
                    long timestamp = currentTimeMillis() - start;
                    double average = model.average();
                    ObservableList<XYChart.Data<Number, Number>> data = series.getData();
                    while (data.size() > 100) {
                        data.remove(0);
                    }
                    data.add(new XYChart.Data<>(timestamp, average));
                }));

        // Start streaming messages
        receiver.on();


        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
