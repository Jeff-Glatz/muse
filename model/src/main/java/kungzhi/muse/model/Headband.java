package kungzhi.muse.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Headband
        implements Serializable {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Battery battery = new Battery();
    private final Configuration configuration = new Configuration();
    private final DrlReference drlReference = new DrlReference();
    private final Version version = new Version();

    public Battery getBattery() {
        return battery;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Version getVersion() {
        return version;
    }

    public DrlReference getDrlReference() {
        return drlReference;
    }

    /**
     * This headband can be considered ready once the configuration
     * has been received. The configuration contains preset information
     * and channel layout that is needed to properly analyze streamed data.
     *
     * @return true if this headband's configuration has been received;
     * false otherwise
     */
    public boolean ready() {
        return !configuration.initial();
    }

    public ModelStream<Configuration> configurationStream() {
        return (headband, configuration) ->
                this.configuration.updateFrom(configuration);
    }

    public ModelStream<Version> versionStream() {
        return (headband, version) ->
                this.version.updateFrom(version);
    }

    public ModelStream<DrlReference> drlReferenceStream() {
        return (headband, drlReference) ->
                this.drlReference.updateFrom(drlReference);
    }

    public ModelStream<Battery> batteryStream() {
        return (headband, battery) ->
                this.battery.updateFrom(battery);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Headband headband = (Headband) o;

        if (battery != null ? !battery.equals(headband.battery) : headband.battery != null) return false;
        if (configuration != null ? !configuration.equals(headband.configuration) : headband.configuration != null)
            return false;
        if (drlReference != null ? !drlReference.equals(headband.drlReference) : headband.drlReference != null)
            return false;
        return version != null ? version.equals(headband.version) : headband.version == null;
    }

    @Override
    public int hashCode() {
        int result = battery != null ? battery.hashCode() : 0;
        result = 31 * result + (configuration != null ? configuration.hashCode() : 0);
        result = 31 * result + (drlReference != null ? drlReference.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
