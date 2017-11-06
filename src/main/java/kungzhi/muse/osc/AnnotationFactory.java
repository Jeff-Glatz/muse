package kungzhi.muse.osc;

import com.jsoniter.any.Any;

import java.io.IOException;

import static kungzhi.muse.osc.SignalSerializer.Annotation.newBuilder;

public class AnnotationFactory
        extends JsonSignalFactory<Annotation> {

    @Override
    protected Annotation create(String path, Any json)
            throws IOException {
        return new Annotation(path, newBuilder()
                .build());
    }
}
