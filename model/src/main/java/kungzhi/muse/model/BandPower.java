package kungzhi.muse.model;

import java.util.stream.Stream;

import static kungzhi.muse.lang.Functions.averageOf;

public class BandPower
        extends AbstractModel {
    private final Band band;
    private final boolean relative;
    private final Values<Float> values;

    public BandPower(long time, Band band, boolean relative, Values<Float> values) {
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

    public Float forChannel(EegChannel channel) {
        return values.at(channel.getIndex());
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

        BandPower bandPower = (BandPower) o;

        if (relative != bandPower.relative) return false;
        if (band != null ? !band.equals(bandPower.band) : bandPower.band != null) return false;
        return values != null ? values.equals(bandPower.values) : bandPower.values == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (band != null ? band.hashCode() : 0);
        result = 31 * result + (relative ? 1 : 0);
        result = 31 * result + (values != null ? values.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BandPower{" +
                "time=" + time +
                ", band=" + band +
                ", relative=" + relative +
                ", values=" + values +
                '}';
    }
}
