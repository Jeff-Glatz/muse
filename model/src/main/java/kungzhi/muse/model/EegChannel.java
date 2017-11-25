package kungzhi.muse.model;

import java.io.Serializable;

public class EegChannel
        implements Serializable, Comparable<EegChannel> {
    private final int index;
    private final Sensor sensor;

    public EegChannel(int index, Sensor sensor) {
        this.index = index;
        this.sensor = sensor;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return sensor.name();
    }

    public Sensor getSensor() {
        return sensor;
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

    @Override
    public String toString() {
        return "EegChannel{" +
                "index=" + index +
                ", sensor=" + sensor +
                '}';
    }
}
