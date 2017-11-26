package kungzhi.muse.ui;

import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.String.format;

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
        boolean completed = false;
        try {
            beforeInitialize();
            onInitialize();
            completed = true;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("failure initializing controller", e);
        } finally {
            afterInitialize(completed);
        }
    }

    protected String localize(String key, Object... args) {
        return format(resources.getString(key), args);
    }

    protected void beforeInitialize()
            throws Exception {
    }

    protected void onInitialize()
            throws Exception {
    }

    protected void afterInitialize(boolean completed) {
    }
}
