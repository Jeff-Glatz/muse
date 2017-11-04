package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

public class HeadbandStatusFactory
        implements SignalFactory<HeadbandStatus> {

    @Override
    public HeadbandStatus create(OSCMessage message) {
        return new HeadbandStatus(message.getName());
    }
}
