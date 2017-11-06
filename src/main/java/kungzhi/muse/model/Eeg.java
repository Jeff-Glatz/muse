package kungzhi.muse.model;

import java.io.Serializable;

public class Eeg
        implements Serializable {
    private final long time;
    private final Float[] values;

    public Eeg(long time, Float[] values) {
        this.time = time;
        this.values = values;
    }
}
