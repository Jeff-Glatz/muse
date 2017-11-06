package kungzhi.muse.osc;

import kungzhi.muse.osc.SignalSerializer.MuseVersion;

public class Version
        extends AbstractSignal {
    private final MuseVersion delegate;

    public Version(String path, MuseVersion delegate) {
        super(path);
        this.delegate = delegate;
    }

    public MuseVersion getDelegate() {
        return delegate;
    }
}
