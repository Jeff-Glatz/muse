package kungzhi.muse.model;

import kungzhi.muse.runtime.ProblemLog;

import static kungzhi.muse.model.HeadbandStatus.State.fromValue;

public class HeadbandStatus
        extends ActiveModel<HeadbandStatus> {
    private Values<Float> values;

    public HeadbandStatus() {
        this(0, null);
    }

    public HeadbandStatus(long time, Values<Float> values) {
        super(time);
        this.values = values;
    }

    public State forChannel(EegChannel channel) {
        return fromValue(values.at(channel.getIndex()).intValue());
    }

    @Override
    public boolean sameAs(HeadbandStatus that) {
        return values != null ? values.equals(that.values) : that.values == null;
    }

    @Override
    protected HeadbandStatus update(HeadbandStatus item) {
        this.time = item.time;
        this.values = item.values;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        return sameAs((HeadbandStatus) o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (values != null ? values.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HeadbandStatus{" +
                "time=" + time +
                ", values=" + values +
                '}';
    }

    @Override
    protected HeadbandStatus newInstance() {
        return new HeadbandStatus();
    }

    public enum State {
        GOOD(1), OK(2), BAD(3);

        private final int value;

        State(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public static State fromValue(int value) {
            switch (value) {
                case 1:
                    return GOOD;
                case 2:
                    return OK;
                case 3:
                    return BAD;
                default:
                    ProblemLog.problem(HeadbandStatus.class, "missing enum item for value: %s", value);
                    return null;
            }
        }
    }
}
