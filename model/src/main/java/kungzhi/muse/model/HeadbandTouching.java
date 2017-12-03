package kungzhi.muse.model;

public class HeadbandTouching
        extends ActiveModel<HeadbandTouching> {
    private Integer value;

    public HeadbandTouching() {
        this(0, null);
    }

    public HeadbandTouching(long time, Integer value) {
        super(time);
        this.value = value;
    }

    public Integer get() {
        return value;
    }

    public boolean isTrue() {
        return value > 0;
    }

    public boolean isFalse() {
        return value <= 0;
    }

    @Override
    public boolean sameAs(HeadbandTouching that) {
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    protected HeadbandTouching update(HeadbandTouching item) {
        this.time = item.time;
        this.value = item.value;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        return sameAs((HeadbandTouching) o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HeadbandTouching{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }

    @Override
    protected HeadbandTouching newInstance() {
        return new HeadbandTouching();
    }
}
