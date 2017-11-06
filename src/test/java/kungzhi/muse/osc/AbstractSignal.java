package kungzhi.muse.osc;

import static kungzhi.muse.osc.SignalMetadata.fromPath;

public abstract class AbstractSignal
        implements Signal {
    private final String path;

    public AbstractSignal(String path) {
        this.path = path;
    }

    @Override
    public SignalMetadata getMetadata() {
        return fromPath(path);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractSignal that = (AbstractSignal) o;

        return path != null ? path.equals(that.path) : that.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
