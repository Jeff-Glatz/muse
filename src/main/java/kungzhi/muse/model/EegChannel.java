package kungzhi.muse.model;

import java.io.Serializable;

public class EegChannel
        implements Serializable, Comparable<EegChannel> {
    private final Sensor sensor;
    private final int index;

    public EegChannel(Sensor sensor, int index) {
        this.sensor = sensor;
        this.index = index;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int compareTo(EegChannel that) {
        if (this.index > that.index) {
            return 1;
        } else if (this.index < that.index) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EegChannel channel = (EegChannel) o;

        if (index != channel.index) return false;
        return sensor == channel.sensor;
    }

    @Override
    public int hashCode() {
        int result = sensor != null ? sensor.hashCode() : 0;
        result = 31 * result + index;
        return result;
    }
}
