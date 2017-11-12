package kungzhi.muse.runtime;

import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.DrlReference;
import kungzhi.muse.model.Headband;
import kungzhi.muse.model.ModelStream;
import kungzhi.muse.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MuseHeadband
        implements Headband {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Configuration configuration = new Configuration();
    private final Version version = new Version();
    private final DrlReference drlReference = new DrlReference();

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public DrlReference getDrlReference() {
        return drlReference;
    }

    public ModelStream<Configuration> configurationStream() {
        return (session, configuration) ->
                this.configuration.updateFrom(configuration);
    }

    public ModelStream<Version> versionStream() {
        return (session, version) ->
                this.version.updateFrom(version);
    }

    public ModelStream<DrlReference> drlReferenceStream() {
        return (session, drlReference) ->
                this.drlReference.updateFrom(drlReference);
    }
}
