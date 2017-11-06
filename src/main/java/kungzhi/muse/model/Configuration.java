package kungzhi.muse.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class Configuration
        implements Serializable {
    private final SortedSet<EegChannel> eegChannelLayout = new TreeSet<>();

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

    public Configuration copyOf() {
        Configuration configuration = new Configuration();
        configuration.updateFrom(this);
        return configuration;
    }

    public void updateFrom(Configuration that) {
        setEegChannelLayout(that.eegChannelLayout);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        return eegChannelLayout.equals(that.eegChannelLayout);
    }

    @Override
    public int hashCode() {
        return eegChannelLayout.hashCode();
    }
}
