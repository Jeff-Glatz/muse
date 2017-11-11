package kungzhi.muse.osc.transform;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Fft;

import static kungzhi.muse.osc.transform.MessageHelper.extractArguments;

public class FftTransformer
        implements MessageTransformer<Fft> {
    private final int channelIndex;

    public FftTransformer(int channelIndex) {
        this.channelIndex = channelIndex;
    }

    @Override
    public Fft fromMessage(long time, OSCMessage message)
            throws Exception {
        return new Fft(time, channelIndex, extractArguments(message, Float.class));
    }
}
