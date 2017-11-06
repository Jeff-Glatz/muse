package kungzhi.muse.osc;

import com.google.protobuf.GeneratedMessageLite;
import de.sciss.net.OSCMessage;

public interface MuseSignalTransformer<Payload extends GeneratedMessageLite<?, ?>> {
    MuseSignal<Payload> fromOsc(OSCMessage message)
            throws Exception;
}
