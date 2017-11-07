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

import static java.lang.System.nanoTime;
import static kungzhi.muse.osc.Path.THETA_RELATIVE;

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
                .run();
    }

    @Override
    public void start(Stage stage)
            throws Exception {
        MessageDispatcher dispatcher = dispatcher();
        MessageReceiver receiver = receiver()
                .withProtocol("tcp")
                .withPort(5000);


        stage.setTitle("Muse EEG Feed");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Timestamp");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Brainwave Monitoring");

        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Theta Relative");
        dispatcher.withStream(THETA_RELATIVE, BandPower.class, (session, model) -> {
            ObservableList data = series.getData();
            data.add(new XYChart.Data(nanoTime(), model.average()));
        });

        // Start streaming messages
        receiver.on();


        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

    private MessageDispatcher dispatcher() {
        return context.getBean(MessageDispatcher.class);
    }

    private MessageReceiver receiver() {
        return context.getBean(MessageReceiver.class);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
