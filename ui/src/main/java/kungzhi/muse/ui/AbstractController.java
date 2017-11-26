package kungzhi.muse.ui;

import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractController
        implements Initializable {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected URL location;
    protected ResourceBundle resources;

    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        log.info("initializing controller from {}", location);
        this.location = location;
        this.resources = resources;
        try {
            initialize();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("failure initializing controller", e);
        }
    }

    protected abstract void initialize()
            throws Exception;
}
