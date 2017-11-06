package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.data.Values;

public class MessageArguments
        implements Values {
    private final OSCMessage message;

    public MessageArguments(OSCMessage message) {
        this.message = message;
    }

    @Override
    public int count() {
        return message.getArgCount();
    }

    @Override
    public <T> T at(int index) {
        return (T) message.getArg(index);
    }
}
