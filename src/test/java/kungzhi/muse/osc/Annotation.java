package kungzhi.muse.osc;

public class Annotation
        extends AbstractSignal {
    private final SignalSerializer.Annotation delegate;

    public Annotation(String path, SignalSerializer.Annotation delegate) {
        super(path);
        this.delegate = delegate;
    }
}
