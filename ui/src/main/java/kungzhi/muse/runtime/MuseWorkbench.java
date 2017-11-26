package kungzhi.muse.runtime;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kungzhi.platform.runtime.SpringBootContext;
import kungzhi.platform.runtime.SpringBootIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.ResourceBundle;

@Import({SpringBootIntegration.class, UiWiring.class})
@SpringBootApplication
public class MuseWorkbench
        extends Application {
    private final SpringBootContext context = new SpringBootContext(this);

    @Autowired
    private ResourceBundle resources;

    @Autowired
    private FXMLLoader loader;

    @Override
    public void init()
            throws Exception {
        super.init();
        context.run();
        loader.setResources(resources);
        loader.setLocation(getClass()
                .getResource("/kungzhi/muse/ui/MuseWorkbench.fxml"));
    }

    @Override
    public void start(Stage stage)
            throws Exception {
        stage.setTitle("Muse Workbench");
        stage.setOnCloseRequest(event -> context.stop());
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
