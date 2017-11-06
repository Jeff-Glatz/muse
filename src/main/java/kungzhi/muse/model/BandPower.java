package kungzhi.muse.model;

import java.util.Arrays;
import java.util.stream.Stream;

public class BandPower
        extends AbstractModel {
    private final Band band;
    private final boolean relative;
    private final Float[] values;

    public BandPower(Band band, boolean relative, long time, Float... values) {
        super(time);
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

    public Float[] getValues() {
        return values;
    }

    public Float forChannel(EegChannel channel) {
        return values[channel.getIndex()];
    }

    public Stream<Float> values() {
        return Stream.of(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BandPower bandPower = (BandPower) o;

        if (relative != bandPower.relative) return false;
        if (band != null ? !band.equals(bandPower.band) : bandPower.band != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(values, bandPower.values);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (band != null ? band.hashCode() : 0);
        result = 31 * result + (relative ? 1 : 0);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
