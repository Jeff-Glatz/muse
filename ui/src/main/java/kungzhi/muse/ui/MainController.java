package kungzhi.muse.ui;

import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class MainController
        extends AbstractController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Initializing controller from {}", location);
    }
}
