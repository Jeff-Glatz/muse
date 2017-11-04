package kungzhi.muse.osc;

public class Annotation
        extends AbstractSignal {
    private final Serializer.Annotation delegate;

    public Annotation(String path, Serializer.Annotation delegate) {
        super(path);
        this.delegate = delegate;
    }
}
