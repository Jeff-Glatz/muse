package kungzhi.muse.osc;

import java.io.Serializable;

public interface Signal
        extends Serializable {
    SignalMetadata getMetadata();

    String getPath();
}
