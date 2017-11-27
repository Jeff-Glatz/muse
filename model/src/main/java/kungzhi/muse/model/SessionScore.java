package kungzhi.muse.model;

import java.util.stream.Stream;

import static kungzhi.muse.lang.Functions.averageOf;

public class SessionScore
        extends AbstractModel {
    private final Band band;
    private final Values<Float> values;

    public SessionScore(long time, Band band, Values<Float> values) {
        super(time);
        this.band = band;
        this.values = values;
    }

    public Band getBand() {
        return band;
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

        SessionScore sessionScore = (SessionScore) o;

        if (band != null ? !band.equals(sessionScore.band) : sessionScore.band != null) return false;
        return values != null ? values.equals(sessionScore.values) : sessionScore.values == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (band != null ? band.hashCode() : 0);
        result = 31 * result + (values != null ? values.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SessionScore{" +
                "time=" + time +
                ", band=" + band +
                ", values=" + values +
                '}';
    }
}
