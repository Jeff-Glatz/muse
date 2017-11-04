package kungzhi.muse.osc;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import static kungzhi.muse.osc.Serializer.MuseConfig.newBuilder;

public class ConfigurationFactory
        extends ProtocolBufferSignalFactory<Configuration> {

    @Override
    protected Configuration create(String path, ByteString data)
            throws InvalidProtocolBufferException {
        return new Configuration(path, newBuilder()
                .mergeFrom(data)
                .build());
    }
}
