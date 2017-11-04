package kungzhi.muse.osc;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import de.sciss.net.OSCMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static de.sciss.net.OSCPacket.printTextOn;
import static kungzhi.muse.osc.MessageHelper.argumentAt;

public abstract class ProtocolBufferSignalFactory<S extends Signal>
        implements SignalFactory<S> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public final S create(OSCMessage message)
            throws IOException {
        ByteString data = copyFromUtf8(argumentAt(message, String.class, 0));
        log.debug("Reading signal from protocol buffer: {}", data.toStringUtf8());
        try {
            return create(message.getName(), data);
        } catch (InvalidProtocolBufferException e) {
            ByteArrayOutputStream messageText = new ByteArrayOutputStream();
            printTextOn(new PrintStream(messageText), message);
            log.error("Failure reading signal from message: {}", messageText.toString());
            throw e;
        }
    }

    protected abstract S create(String path, ByteString data)
            throws IOException;
}
