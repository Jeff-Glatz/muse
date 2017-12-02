package kungzhi.muse.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static java.lang.String.format;
import static javafx.application.Platform.runLater;

@Controller
public class HeadbandDetailsController
        extends AbstractController {
    private final Headband headband;

    @FXML
    private TextField macAddress;

    @FXML
    private TextField serialNumber;

    @FXML
    private TextField preset;

    @FXML
    private TextField channelCount;

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
    public HeadbandDetailsController(Headband headband) {
        this.headband = headband;
    }

    @Override
    protected void onInitialize() {
        Configuration configuration = headband.getConfiguration();
        configuration.addActiveItemListener((current, previous) ->
                runLater(this::updateDetailsView));
    }

    private void updateDetailsView() {
        Configuration configuration = headband.getConfiguration();
        macAddress.setText(configuration.getMacAddress());
        serialNumber.setText(configuration.getSerialNumber());
        preset.setText(localize(configuration.getPreset()));
        channelCount.setText(format("%d", configuration.getEegChannelCount()));

        Version version = headband.getVersion();
        buildNumber.setText(version.getBuildNumber());
        firmwareType.setText(version.getFirmwareType());
        hardwareVersion.setText(version.getHardwareVersion());
        firmwareHeadsetVersion.setText(version.getFirmwareHeadsetVersion());
        firmwareBootloaderVersion.setText(version.getFirmwareBootloaderVersion());
        protocolVersion.setText(version.getProtocolVersion());
    }
}
