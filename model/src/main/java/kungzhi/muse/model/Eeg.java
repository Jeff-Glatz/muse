package kungzhi.muse.model;

import java.util.stream.Stream;

import static kungzhi.muse.lang.Functions.averageOf;

public class Eeg
        extends AbstractModel {
    private final Values<Float> values;

    public Eeg(long time, Values<Float> values) {
        super(time);
        this.values = values;
    }

    public Stream<Float> values() {
        return values.streamOf();
    }

    public Double average() {
        return averageOf(values(), 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Eeg eeg = (Eeg) o;

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
        return "Eeg{" +
                "time=" + time +
                ", values=" + values +
                '}';
    }
}
