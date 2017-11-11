package kungzhi.muse.model;

import static kungzhi.muse.model.HeadbandStatusStrict.Status.fromValue;

public class HeadbandStatusStrict
        extends AbstractModel {
    private final Values<Float> values;

    public HeadbandStatusStrict(long time, Values<Float> values) {
        super(time);
        this.values = values;
    }

    public Status forChannel(EegChannel channel) {
        return fromValue(values.at(channel.getIndex()).intValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        HeadbandStatusStrict eeg = (HeadbandStatusStrict) o;

        return values != null ? values.equals(eeg.values) : eeg.values == null;
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

    public enum Status {
        GOOD(0), BAD(1);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public static Status fromValue(int value) {
            switch (value) {
                case 0:
                    return GOOD;
                case 1:
                    return BAD;
                default:
                    return null;
            }
        }
    }
}
