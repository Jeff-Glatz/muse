package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Band;
import kungzhi.muse.model.BandPower;

import static kungzhi.muse.osc.OSCMessageHelper.collectArguments;

public class BandPowerTransformer
        implements OSCMessageTransformer<BandPower> {
    private final Band band;
    private final boolean relative;

    public BandPowerTransformer(Band band, boolean relative) {
        this.band = band;
        this.relative = relative;
    }

    @Override
    public BandPower fromMessage(OSCMessage message)
            throws Exception {
        return new BandPower(band, relative, collectArguments(message));
    }
}
