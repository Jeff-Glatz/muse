package kungzhi.muse.model;

public class Accelerometer
        extends AbstractModel {
    private final Float x;
    private final Float y;
    private final Float z;

    public Accelerometer(long time, Float x, Float y, Float z) {
        super(time);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public Float getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Accelerometer that = (Accelerometer) o;

        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (y != null ? !y.equals(that.y) : that.y != null) return false;
        return z != null ? z.equals(that.z) : that.z == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Accelerometer{" +
                "time=" + time +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
