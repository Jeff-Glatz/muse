package kungzhi.muse.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.HeadbandTouching;
import kungzhi.muse.model.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.time.Clock;

import static java.lang.String.format;
import static javafx.application.Platform.runLater;

@Controller
public class MainController
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
    private TextField hardwareVersion;

    @FXML
    private TextField firmwareHeadsetVersion;

    @Autowired
    public MainController(Clock clock, Headband headband) {
        this.headband = headband;
    }

    @PostConstruct
    public void registerHeadbandListeners() {
        Configuration configuration = headband.getConfiguration();
        configuration.addActiveItemListener((current, previous) -> {
            runLater(this::updateDetailsView);
        });

        HeadbandTouching touching = headband.getTouching();
        touching.addActiveItemListener(((current, previous) -> {
        }));
    }

    private void updateDetailsView() {
        Configuration configuration = headband.getConfiguration();
        macAddress.setText(configuration.getMacAddress());
        serialNumber.setText(configuration.getSerialNumber());
        preset.setText(configuration.getPreset().getId());
        channelCount.setText(format("%d", configuration.getEegChannelCount()));

        Version version = headband.getVersion();
        hardwareVersion.setText(version.getHardwareVersion());
        firmwareHeadsetVersion.setText(version.getFirmwareHeadsetVersion());
    }
}
