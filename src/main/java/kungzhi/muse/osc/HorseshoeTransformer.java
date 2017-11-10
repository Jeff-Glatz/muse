package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Horseshoe;

import static kungzhi.muse.osc.MessageHelper.extractArguments;

public class HorseshoeTransformer
        implements MessageTransformer<Horseshoe> {

    @Override
    public Horseshoe fromMessage(long time, OSCMessage message)
            throws Exception {
        return new Horseshoe(time, extractArguments(message, Float.class));
    }
}
