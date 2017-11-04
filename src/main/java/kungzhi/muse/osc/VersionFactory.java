package kungzhi.muse.osc;

import com.google.protobuf.ByteString;

import java.io.IOException;

import static kungzhi.muse.osc.Serializer.MuseVersion.newBuilder;

public class VersionFactory
        extends ProtocolBufferSignalFactory<Version> {

    @Override
    protected Version create(String path, ByteString data)
            throws IOException {
        return new Version(path, newBuilder()
                .mergeFrom(data)
                .build());
    }
}
