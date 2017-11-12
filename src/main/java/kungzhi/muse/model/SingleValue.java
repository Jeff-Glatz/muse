package kungzhi.muse.model;

public class SingleValue<V>
        extends AbstractModel {
    private final V value;

    public SingleValue(long time, V value) {
        super(time);
        this.value = value;
    }

    public V get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SingleValue<?> that = (SingleValue<?>) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SingleValue{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
