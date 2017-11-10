package kungzhi.muse.config;

import kungzhi.muse.model.Battery;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.DrlReference;
import kungzhi.muse.model.ModelStream;
import kungzhi.muse.model.Session;
import kungzhi.muse.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MuseSession
        implements Session {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Configuration configuration;
    private final Version version;
    private final Battery battery;
    private final DrlReference drlReference;

    @Autowired
    public MuseSession(Configuration configuration, Version version,
                       Battery battery, DrlReference drlReference) {
        this.configuration = configuration;
        this.version = version;
        this.battery = battery;
        this.drlReference = drlReference;
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
            this.configuration.updateFrom(configuration);
        };
    }

    public ModelStream<Version> versionStream() {
        return (session, version) -> {
            this.version.updateFrom(version);
        };
    }

    public ModelStream<Battery> batteryStream() {
        return (session, battery) -> {
            this.battery.updateFrom(battery);
        };
    }

    public ModelStream<DrlReference> drlReferenceStream() {
        return (session, drlReference) -> {
            this.drlReference.updateFrom(drlReference);
        };
    }
}
