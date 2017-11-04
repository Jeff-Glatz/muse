package kungzhi.muse.osc;

import com.google.protobuf.ByteString;

import java.io.IOException;

import static kungzhi.muse.osc.Serializer.Annotation.newBuilder;

public class AnnotationFactory
        extends ProtocolBufferSignalFactory<Annotation> {

    @Override
    protected Annotation create(String path, ByteString data)
            throws IOException {
        return new Annotation(path, newBuilder()
                .mergeFrom(data)
                .build());
    }
}
