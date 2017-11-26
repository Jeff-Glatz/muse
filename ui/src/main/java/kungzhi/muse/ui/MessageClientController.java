package kungzhi.muse.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import kungzhi.muse.osc.service.MessageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class MessageClientController
        extends AbstractController {
    private final MessageClient client;

    @FXML
    private ToggleButton clientToggleButton;

    @Autowired
    public MessageClientController(MessageClient client) {
        this.client = client;
    }

    @FXML
    public void toggleClient()
            throws IOException {
        if (clientToggleButton.isSelected()) {
            client.on();
            clientToggleButton.setText("On");
        } else {
            client.off();
            clientToggleButton.setText("Off");
        }
    }
}
