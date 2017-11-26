package kungzhi.muse.runtime;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

public class MuseDesktop
        extends Application {
    private final SpringContext context = new SpringContext(this);

    @Autowired
    private FXMLLoader loader;

    @Override
    public void init()
            throws Exception {
        super.init();
        context.init();
    }

    @Override
    public void start(Stage stage)
            throws Exception {
        loader.setLocation(getClass().getResource("MuseDesktop.fxml"));
        stage.setTitle("Muse Desktop");
        stage.setOnCloseRequest(event -> {
            context.close();
        });
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
