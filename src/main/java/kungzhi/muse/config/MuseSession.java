package kungzhi.muse.config;

import kungzhi.muse.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public MuseSession() {
        this(new Configuration());
    }

    MuseSession(Configuration configuration) {
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

    public ModelStream<Configuration> asConfigurationStream() {
        return (session, configuration) -> {
            if (!this.configuration.equals(configuration)) {
                Configuration previous = this.configuration.copyOf();
                this.configuration.updateFrom(configuration);
                log.info("Current configuration has been updated: {}", configuration);
                sessionListeners.forEach(listener ->
                        listener.configurationChanged(previous, this.configuration));
            }
        };
    }

    public ModelStream<Version> asVersionStream() {
        return (session, version) -> {
            this.version = version;
        };
    }

    public ModelStream<Battery> asBatteryStream() {
        return (session, battery) -> {
            this.battery = battery;
        };
    }

    public ModelStream<DrlReference> asDrlReferenceStream() {
        return (session, drlReference) -> {
            this.drlReference = drlReference;
        };
    }
}
