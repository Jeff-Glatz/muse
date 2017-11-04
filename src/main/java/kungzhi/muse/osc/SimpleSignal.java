package kungzhi.muse.osc;

public class SimpleSignal
        extends AbstractSignal {
    private final Float value;

    public SimpleSignal(String path, Float value) {
        super(path);
        this.value = value;
    }

    public Float getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SimpleSignal that = (SimpleSignal) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
