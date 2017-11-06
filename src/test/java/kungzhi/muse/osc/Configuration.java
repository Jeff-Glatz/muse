package kungzhi.muse.osc;

import kungzhi.muse.osc.SignalSerializer.MuseConfig;

public class Configuration
        extends AbstractSignal {
    private final MuseConfig delegate;

    public Configuration(String path, MuseConfig delegate) {
        super(path);
        this.delegate = delegate;
    }
}
