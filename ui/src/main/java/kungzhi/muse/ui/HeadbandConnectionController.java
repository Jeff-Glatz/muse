package kungzhi.muse.ui;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import kungzhi.muse.lang.ServiceControl;
import kungzhi.muse.osc.service.MessageClient;
import kungzhi.muse.platform.MuseIO;
import org.controlsfx.control.ToggleSwitch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

import static javafx.application.Platform.runLater;

@Controller
public class HeadbandConnectionController
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
    public HeadbandConnectionController(MuseIO museIO, MessageClient client, NotificationControl notificationControl) {
        this.museIO = museIO;
        this.client = client;
        this.notificationControl = notificationControl;
    }

    @PostConstruct
    public void initialize() {
        museIO.addMusePairingListener(paired -> {
            runLater(() -> {
                if (paired) {
                    museIOToggle.pseudoClassStateChanged(disconnectedPseudoClass, false);
                    notificationControl.notification(localize("model.headband.paired"));
                } else {
                    museIOToggle.pseudoClassStateChanged(disconnectedPseudoClass, true);
                    notificationControl.notification(localize("model.headband.not-paired"));
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
