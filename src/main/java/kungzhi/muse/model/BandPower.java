package kungzhi.muse.model;

import java.io.Serializable;
import java.util.List;

import static java.util.Arrays.asList;

public class BandPower
        implements Serializable {
    private final Band band;
    private final boolean relative;
    private final List<Float> values;

    public BandPower(Band band, boolean relative, Float... values) {
        this.band = band;
        this.relative = relative;
        this.values = asList(values);
    }

    public  BandPower(Band band, boolean relative, List<Float> values) {
        this.band = band;
        this.relative = relative;
        this.values = values;
    }

    public Band getBand() {
        return band;
    }

    public boolean isRelative() {
        return relative;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values.clear();
        this.values.addAll(values);
    }

    public Float forChannel(EegChannel channel) {
        return values.get(channel.getIndex());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BandPower bandPower = (BandPower) o;

        if (relative != bandPower.relative) return false;
        if (band != null ? !band.equals(bandPower.band) : bandPower.band != null) return false;
        return values != null ? values.equals(bandPower.values) : bandPower.values == null;
    }

    @Override
    public int hashCode() {
        int result = band != null ? band.hashCode() : 0;
        result = 31 * result + (relative ? 1 : 0);
        result = 31 * result + (values != null ? values.hashCode() : 0);
        return result;
    }
}
