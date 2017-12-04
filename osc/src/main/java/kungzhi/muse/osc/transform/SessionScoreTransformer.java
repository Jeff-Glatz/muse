package kungzhi.muse.osc.transform;

import com.illposed.osc.OSCMessage;
import kungzhi.muse.model.Band;
import kungzhi.muse.model.SessionScore;

import static kungzhi.muse.osc.transform.MessageHelper.extractArguments;

public class SessionScoreTransformer
        implements MessageTransformer<SessionScore> {
    private final Band band;

    public SessionScoreTransformer(Band band) {
        this.band = band;
    }

    @Override
    public SessionScore fromMessage(long time, OSCMessage message)
            throws Exception {
        return new SessionScore(time, band, extractArguments(message));
    }
}
