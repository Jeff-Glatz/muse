package kungzhi.muse.osc;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jsoniter.any.Any;

import static kungzhi.muse.osc.SignalSerializer.MuseConfig.newBuilder;

public class ConfigurationFactory
        extends JsonSignalFactory<Configuration> {

    @Override
    protected Configuration create(String path, Any json)
            throws InvalidProtocolBufferException {
        return new Configuration(path, newBuilder()
                .build());
    }
}
