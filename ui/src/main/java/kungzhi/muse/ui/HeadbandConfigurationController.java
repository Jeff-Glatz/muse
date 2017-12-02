package kungzhi.muse.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Headband;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static java.lang.String.format;
import static javafx.application.Platform.runLater;

@Controller
public class HeadbandConfigurationController
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


    @Autowired
    public HeadbandConfigurationController(Headband headband) {
        this.headband = headband;
    }

    @Override
    protected void onInitialize() {
        Configuration configuration = headband.getConfiguration();
        configuration.addActiveItemListener((current, previous) ->
                runLater(() -> update(current)));
    }

    private void update(Configuration configuration) {
        macAddress.setText(configuration.getMacAddress());
        serialNumber.setText(configuration.getSerialNumber());
        preset.setText(localize(configuration.getPreset()));
        channelCount.setText(format("%d", configuration.getEegChannelCount()));
    }
}
