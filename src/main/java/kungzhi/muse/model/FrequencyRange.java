package kungzhi.muse.model;

import java.io.Serializable;

import static java.lang.Float.parseFloat;

public class FrequencyRange
        implements Serializable {
    private final Float lower;
    private final Float upper;

    public FrequencyRange(Float lower, Float upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public FrequencyRange(String lower, String upper) {
        this(parseFloat(lower), parseFloat(upper));
    }

    public Float getLower() {
        return lower;
    }

    public Float getUpper() {
        return upper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FrequencyRange that = (FrequencyRange) o;

        if (lower != null ? !lower.equals(that.lower) : that.lower != null) return false;
        return upper != null ? upper.equals(that.upper) : that.upper == null;
    }

    @Override
    public int hashCode() {
        int result = lower != null ? lower.hashCode() : 0;
        result = 31 * result + (upper != null ? upper.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FrequencyRange{" +
                "lower=" + lower +
                ", upper=" + upper +
                '}';
    }
}
