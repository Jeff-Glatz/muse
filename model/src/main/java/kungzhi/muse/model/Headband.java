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
    private final HeadbandStatus status = new HeadbandStatus();
    private final HeadbandTouching touching = new HeadbandTouching();
    private final Version version = new Version();
    private final DrlReference drlReference = new DrlReference();

    public Battery getBattery() {
        return battery;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public HeadbandStatus getStatus() {
        return status;
    }

    public HeadbandTouching getTouching() {
        return touching;
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

    public ModelStream<HeadbandStatus> statusStream() {
        return ((headband, status) ->
                this.status.updateFrom(status));
    }

    public ModelStream<HeadbandTouching> touchingStream() {
        return ((headband, touching) ->
                this.touching.updateFrom(touching));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Headband headband = (Headband) o;

        if (!battery.equals(headband.battery)) return false;
        if (!configuration.equals(headband.configuration)) return false;
        if (!status.equals(headband.status)) return false;
        if (!touching.equals(headband.touching)) return false;
        if (!version.equals(headband.version)) return false;
        return drlReference.equals(headband.drlReference);
    }

    @Override
    public int hashCode() {
        int result = battery.hashCode();
        result = 31 * result + configuration.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + touching.hashCode();
        result = 31 * result + version.hashCode();
        result = 31 * result + drlReference.hashCode();
        return result;
    }
}
