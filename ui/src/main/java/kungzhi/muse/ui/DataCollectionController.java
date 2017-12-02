package kungzhi.muse.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import kungzhi.muse.osc.service.MessageClient;
import kungzhi.muse.osc.service.MuseIO;
import kungzhi.muse.osc.service.OscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DataCollectionController
        extends AbstractController {
    private static final String TOGGLE_ON = "toggle-on";
    private static final String TOGGLE_OFF = "toggle-off";
    private static final String[] TOGGLE_CLASSES = {TOGGLE_ON, TOGGLE_OFF};

    private final MuseIO museIO;
    private final MessageClient client;

    @FXML
    private ToggleButton museIOToggleButton;

    @FXML
    private ToggleButton clientToggleButton;

    @Autowired
    public DataCollectionController(MuseIO museIO, MessageClient client) {
        this.museIO = museIO;
        this.client = client;
    }

    @FXML
    public void toggleMuseIO()
            throws Exception {
        toggle(museIOToggleButton, museIO);
    }

    @FXML
    public void toggleClient()
            throws Exception {
        toggle(clientToggleButton, client);
    }

    private void toggle(ToggleButton button, OscService service)
            throws Exception {
        ObservableList<String> styles = button.getStyleClass();
        styles.removeAll(TOGGLE_CLASSES);
        if (button.isSelected()) {
            service.on();
            button.setText(resources.getString("label.text.on"));
            styles.add(TOGGLE_ON);
        } else {
            service.off();
            button.setText(resources.getString("label.text.off"));
            styles.add(TOGGLE_OFF);
        }
    }
}
