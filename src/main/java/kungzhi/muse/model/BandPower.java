package kungzhi.muse.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

public class BandPower
        implements Serializable {
    private final Band band;
    private final boolean relative;
    private final long time;
    private final Float[] values;

    public BandPower(Band band, boolean relative, long time, Float... values) {
        this.band = band;
        this.relative = relative;
        this.time = time;
        this.values = values;
    }

    public Band getBand() {
        return band;
    }

    public boolean isRelative() {
        return relative;
    }

    public long getTime() {
        return time;
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

        BandPower bandPower = (BandPower) o;

        if (relative != bandPower.relative) return false;
        if (time != bandPower.time) return false;
        if (band != null ? !band.equals(bandPower.band) : bandPower.band != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(values, bandPower.values);
    }

    @Override
    public int hashCode() {
        int result = band != null ? band.hashCode() : 0;
        result = 31 * result + (relative ? 1 : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
