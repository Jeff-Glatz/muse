package kungzhi.muse.model;

import java.io.Serializable;

public class Band
        implements Serializable {
    private String name;
    private FrequencyRange range;

    public Band() {
        this(null, null);
    }

    public Band(String name, FrequencyRange range) {
        this.name = name;
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FrequencyRange getRange() {
        return range;
    }

    public void setRange(FrequencyRange range) {
        this.range = range;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Band band = (Band) o;

        if (name != null ? !name.equals(band.name) : band.name != null) return false;
        return range != null ? range.equals(band.range) : band.range == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (range != null ? range.hashCode() : 0);
        return result;
    }
}
