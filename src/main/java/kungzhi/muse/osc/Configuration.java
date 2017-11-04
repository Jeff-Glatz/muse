package kungzhi.muse.osc;

public class Configuration
        extends AbstractSignal {
    private final Serializer.MuseConfig delegate;

    public Configuration(String path, Serializer.MuseConfig delegate) {
        super(path);
        this.delegate = delegate;
    }
}
