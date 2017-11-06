package kungzhi.muse.model;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class Configuration {
    private final SortedSet<EegChannel> eegChannelLayout = new TreeSet<>();

    public SortedSet<EegChannel> getEegChannelLayout() {
        return eegChannelLayout;
    }

    public void setEegChannelLayout(Collection<EegChannel> eegChannelLayout) {
        this.eegChannelLayout.clear();
        this.eegChannelLayout.addAll(eegChannelLayout);
    }

    public void addEegChannelToLayout(Sensor channelId) {
        this.eegChannelLayout.add(new EegChannel(channelId,
                this.eegChannelLayout.size()));
    }

    public EegChannel eegChannel(Sensor channelId) {
        return eegChannelLayout.stream()
                .filter(eegChannel -> eegChannel.getSensor() == channelId)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
