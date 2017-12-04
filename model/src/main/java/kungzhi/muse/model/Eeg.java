package kungzhi.muse.model;

import java.util.List;
import java.util.stream.Stream;

import static kungzhi.lang.Functions.averageOf;

public class Eeg
        extends AbstractModel {
    private final List<Object> values;

    public Eeg(long time, List<Object> values) {
        super(time);
        this.values = values;
    }

    public Stream<Float> values() {
        return values.stream()
                .map(Float.class::cast);
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
