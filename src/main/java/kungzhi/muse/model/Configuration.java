package kungzhi.muse.model;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

@Component
public class Configuration
        extends ActiveModel<Configuration> {
    private final SortedSet<EegChannel> eegChannelLayout = new TreeSet<>();

    public Configuration() {
        super(0);
    }

    public Configuration(long time) {
        super(time);
    }

    public SortedSet<EegChannel> getEegChannelLayout() {
        return eegChannelLayout;
    }

    public void setEegChannelLayout(Collection<EegChannel> eegChannelLayout) {
        this.eegChannelLayout.clear();
        this.eegChannelLayout.addAll(eegChannelLayout);
    }

    public Configuration withEegChannelInLayout(Sensor sensor) {
        this.eegChannelLayout.add(new EegChannel(sensor,
                this.eegChannelLayout.size()));
        return this;
    }

    public EegChannel eegChannel(Sensor channelId) {
        return eegChannelLayout.stream()
                .filter(eegChannel -> eegChannel.getSensor() == channelId)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean differsFrom(Configuration that) {
        return !this.sameAs(that);
    }

    public Configuration copyFrom(Configuration that) {
        this.time = that.time;
        this.eegChannelLayout.clear();
        this.eegChannelLayout.addAll(that.eegChannelLayout);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        return sameAs((Configuration) o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + eegChannelLayout.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "time=" + time +
                ", eegChannelLayout=" + eegChannelLayout +
                '}';
    }

    @Override
    protected Configuration newInstance() {
        return new Configuration();
    }

    private boolean sameAs(Configuration that) {
        return eegChannelLayout.equals(that.eegChannelLayout);
    }
}
