package kungzhi.muse.osc;

import com.google.protobuf.GeneratedMessageLite;

import static kungzhi.muse.osc.SignalMetadata.fromPath;

public class MuseSignal<Payload extends GeneratedMessageLite<?, ?>> {
    private final String path;
    private final Payload payload;

    public MuseSignal(String path, Payload payload) {
        this.path = path;
        this.payload = payload;
    }

    public SignalMetadata getMetadata() {
        return fromPath(path);
    }

    public String getPath() {
        return path;
    }

    public Payload getPayload() {
        return payload;
    }
}
