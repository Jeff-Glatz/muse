package kungzhi.muse.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static javafx.application.Platform.runLater;

@Controller
public class HeadbandVersionController
        extends AbstractController {
    private final Headband headband;

    @FXML
    private TextField buildNumber;

    @FXML
    private TextField firmwareType;

    @FXML
    private TextField hardwareVersion;

    @FXML
    private TextField firmwareHeadsetVersion;

    @FXML
    private TextField firmwareBootloaderVersion;

    @FXML
    private TextField protocolVersion;


    @Autowired
    public HeadbandVersionController(Headband headband) {
        this.headband = headband;
    }

    @Override
    protected void onInitialize() {
        Version version = headband.getVersion();
        version.addActiveItemListener((current, previous) ->
                runLater(() -> update(current)));
    }

    private void update(Version version) {
        buildNumber.setText(version.getBuildNumber());
        firmwareType.setText(version.getFirmwareType());
        hardwareVersion.setText(version.getHardwareVersion());
        firmwareHeadsetVersion.setText(version.getFirmwareHeadsetVersion());
        firmwareBootloaderVersion.setText(version.getFirmwareBootloaderVersion());
        protocolVersion.setText(version.getProtocolVersion());
    }
}
