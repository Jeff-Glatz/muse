package kungzhi.muse.osc;

public class Version
        extends AbstractSignal {
    private final Serializer.MuseVersion delegate;

    public Version(String path, Serializer.MuseVersion delegate) {
        super(path);
        this.delegate = delegate;
    }

    public Serializer.MuseVersion getDelegate() {
        return delegate;
    }
}
