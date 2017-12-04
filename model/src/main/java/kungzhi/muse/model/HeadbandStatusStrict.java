package kungzhi.muse.model;

import kungzhi.muse.runtime.ProblemLog;

import java.util.List;

import static kungzhi.muse.model.HeadbandStatusStrict.State.fromValue;

public class HeadbandStatusStrict
        extends ActiveModel<HeadbandStatusStrict> {
    private List<Object> values;

    public HeadbandStatusStrict() {
        this(0, null);
    }

    public HeadbandStatusStrict(long time, List<Object> values) {
        super(time);
        this.values = values;
    }

    public State forChannel(EegChannel channel) {
        Float value = (Float) values.get(channel.getIndex());
        return fromValue(value.intValue());
    }

    @Override
    public boolean sameAs(HeadbandStatusStrict that) {
        return values != null ? values.equals(that.values) : that.values == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        return sameAs((HeadbandStatusStrict) o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (values != null ? values.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HeadbandStatusStrict{" +
                "time=" + time +
                ", values=" + values +
                '}';
    }

    @Override
    protected HeadbandStatusStrict newInstance() {
        return new HeadbandStatusStrict();
    }

    @Override
    protected HeadbandStatusStrict update(HeadbandStatusStrict item) {
        this.time = item.time;
        this.values = item.values;
        return this;
    }

    public enum State {
        GOOD(0), BAD(1);

        private final int value;

        State(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public static State fromValue(int value) {
            switch (value) {
                case 0:
                    return GOOD;
                case 1:
                    return BAD;
                default:
                    ProblemLog.problem(HeadbandStatusStrict.class, "missing enum item for value: %s", value);
                    return null;
            }
        }
    }
}
