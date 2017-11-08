package kungzhi.muse.config;

import kungzhi.muse.model.Battery;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.DrlReference;
import kungzhi.muse.model.ModelStream;
import kungzhi.muse.model.Session;
import kungzhi.muse.model.SessionListener;
import kungzhi.muse.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MuseSession
        implements Session {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final List<SessionListener> sessionListeners = new ArrayList<>();
    private final Configuration configuration;

    private Version version;
    private Battery battery;
    private DrlReference drlReference;

    @Autowired
    public MuseSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void addSessionListener(SessionListener listener) {
        sessionListeners.add(listener);
    }

    @Override
    public void removeSessionListener(SessionListener listener) {
        sessionListeners.remove(listener);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public Battery getBattery() {
        return battery;
    }

    @Override
    public DrlReference getDrlReference() {
        return drlReference;
    }

    public ModelStream<Configuration> configurationStream() {
        return (session, configuration) -> {
            if (!this.configuration.needsUpdate(configuration)) {
                Configuration previous = this.configuration.copyOf();
                this.configuration.updateFrom(configuration);
                log.info("Current configuration has been updated: {}", configuration);
                sessionListeners.forEach(listener ->
                        listener.configurationChanged(previous, this.configuration));
            }
        };
    }

    public ModelStream<Version> versionStream() {
        return (session, version) -> {
            this.version = version;
        };
    }

    public ModelStream<Battery> batteryStream() {
        return (session, battery) -> {
            this.battery = battery;
        };
    }

    public ModelStream<DrlReference> drlReferenceStream() {
        return (session, drlReference) -> {
            this.drlReference = drlReference;
        };
    }
}
