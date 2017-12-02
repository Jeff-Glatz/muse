package kungzhi.muse.ui;

import javafx.fxml.FXML;
import kungzhi.muse.lang.ServiceControl;
import kungzhi.muse.osc.service.MessageClient;
import kungzhi.muse.platform.MuseIO;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.ToggleSwitch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

import static java.lang.String.format;
import static javafx.application.Platform.runLater;

@Controller
public class HeadbandConnectionController
        extends AbstractController {
    private static final String TOGGLE_ON = "toggle-on";
    private static final String TOGGLE_OFF = "toggle-off";
    private static final String[] TOGGLE_CLASSES = {TOGGLE_ON, TOGGLE_OFF};

    private final MuseIO museIO;
    private final MessageClient client;

    @FXML
    private ToggleSwitch museIOToggle;

    @FXML
    private ToggleSwitch clientToggle;

    @Autowired
    public HeadbandConnectionController(MuseIO museIO, MessageClient client) {
        this.museIO = museIO;
        this.client = client;
    }

    @PostConstruct
    public void initialize() {
        museIO.addMusePairingListener(paired -> {
            runLater(() -> {
                String message = localize(format("model.headband.%s", paired ? "paired" : "not-paired"));
                Notifications notifications = Notifications.create()
                        .title(localize("model.headband.monitor"))
                        .text(message);
                if (paired) {
                    notifications.showInformation();
                } else {
                    notifications.showWarning();
                }
            });
        });
    }

    protected void onInitialize() {
        museIOToggle.selectedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    try {
                        toggle(museIOToggle, museIO);
                    } catch (Exception e) {
                        log.error("Failure toggling MuseIO state", e);
                    }
                });
        clientToggle.selectedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    try {
                        toggle(clientToggle, client);
                    } catch (Exception e) {
                        log.error("Failure toggling OSC client state", e);
                    }
                });
    }

    private void toggle(ToggleSwitch button, ServiceControl control)
            throws Exception {
        if (button.isSelected()) {
            control.on();
        } else {
            control.off();
        }
    }
}
