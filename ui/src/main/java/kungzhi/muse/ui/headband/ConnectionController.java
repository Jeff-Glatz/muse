package kungzhi.muse.ui.headband;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import kungzhi.muse.lang.ServiceControl;
import kungzhi.muse.osc.service.MessageClient;
import kungzhi.muse.platform.MuseIO;
import kungzhi.muse.ui.common.AbstractController;
import kungzhi.muse.ui.common.NotificationControl;
import org.controlsfx.control.ToggleSwitch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

import static javafx.application.Platform.runLater;

@Controller
public class ConnectionController
        extends AbstractController {
    private final PseudoClass disconnectedPseudoClass = PseudoClass.getPseudoClass("disconnected");
    private final MuseIO museIO;
    private final MessageClient client;
    private final NotificationControl notificationControl;

    @FXML
    private ToggleSwitch museIOToggle;

    @FXML
    private ToggleSwitch clientToggle;

    @Autowired
    public ConnectionController(MuseIO museIO, MessageClient client, NotificationControl notificationControl) {
        this.museIO = museIO;
        this.client = client;
        this.notificationControl = notificationControl;
    }

    @PostConstruct
    public void initialize() {
        museIO.addConnectionListener(connected -> {
            runLater(() -> {
                if (connected) {
                    museIOToggle.pseudoClassStateChanged(disconnectedPseudoClass, false);
                    notificationControl.notification(localize("model.headband.connected"));
                } else {
                    museIOToggle.pseudoClassStateChanged(disconnectedPseudoClass, true);
                    notificationControl.notification(localize("model.headband.disconnected"));
                }
            });
        });
    }

    protected void onInitialize() {
        museIOToggle.selectedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    museIOToggle.pseudoClassStateChanged(disconnectedPseudoClass, false);
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
