package kungzhi.muse.model;

public class DrlReference
        extends AbstractModel {
    private final Float drivenRightLegVoltage;
    private final Float referenceVoltage;

    public DrlReference(long time, Float drivenRightLegVoltage, Float referenceVoltage) {
        super(time);
        this.drivenRightLegVoltage = drivenRightLegVoltage;
        this.referenceVoltage = referenceVoltage;
    }

    public Float getDrivenRightLegVoltage() {
        return drivenRightLegVoltage;
    }

    public Float getReferenceVoltage() {
        return referenceVoltage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DrlReference that = (DrlReference) o;

        if (drivenRightLegVoltage != null ? !drivenRightLegVoltage.equals(that.drivenRightLegVoltage) : that.drivenRightLegVoltage != null)
            return false;
        return referenceVoltage != null ? referenceVoltage.equals(that.referenceVoltage) : that.referenceVoltage == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (drivenRightLegVoltage != null ? drivenRightLegVoltage.hashCode() : 0);
        result = 31 * result + (referenceVoltage != null ? referenceVoltage.hashCode() : 0);
        return result;
    }
}
