package kungzhi.muse.model;

import static kungzhi.muse.model.HeadbandStatus.Status.fromValue;
import static kungzhi.muse.runtime.ProblemLog.problem;

public class HeadbandStatus
        extends AbstractModel
        implements Equivalence<HeadbandStatus> {
    private final Values<Float> values;

    public HeadbandStatus(long time, Values<Float> values) {
        super(time);
        this.values = values;
    }

    public Status forChannel(EegChannel channel) {
        return fromValue(values.at(channel.getIndex()).intValue());
    }

    @Override
    public boolean sameAs(HeadbandStatus that) {
        return values != null ? values.equals(that.values) : that.values == null;
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

    public enum Status {
        GOOD(1), OK(2), BAD(3);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public static Status fromValue(int value) {
            switch (value) {
                case 1:
                    return GOOD;
                case 2:
                    return OK;
                case 3:
                    return BAD;
                default:
                    problem(HeadbandStatus.class, "missing enum item for value: %s", value);
                    return null;
            }
        }
    }
}
