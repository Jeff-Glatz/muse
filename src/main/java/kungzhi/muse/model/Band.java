package kungzhi.muse.model;

import java.io.Serializable;

public class Band
        implements Serializable {
    private String identifier;
    private FrequencyRange range;

    public Band() {
        this(null, null);
    }

    public Band(String identifier, FrequencyRange range) {
        this.identifier = identifier;
        this.range = range;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

        if (identifier != null ? !identifier.equals(band.identifier) : band.identifier != null) return false;
        return range != null ? range.equals(band.range) : band.range == null;
    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + (range != null ? range.hashCode() : 0);
        return result;
    }
}
