package kungzhi.muse.model;

public abstract class AbstractModel
        implements Model {
    private long time;

    public AbstractModel(long time) {
        this.time = time;
    }

    @Override
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractModel that = (AbstractModel) o;

        return time == that.time;
    }

    @Override
    public int hashCode() {
        return (int) (time ^ (time >>> 32));
    }
}
