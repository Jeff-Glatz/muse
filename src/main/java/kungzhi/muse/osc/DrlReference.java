package kungzhi.muse.osc;

public class DrlReference
        extends AbstractSignal {
    private final float drivenRightLegVoltage;
    private final float referenceVoltage;

    public DrlReference(String path, Float drivenRightLegVoltage, Float referenceVoltage) {
        super(path);
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

        if (Float.compare(that.drivenRightLegVoltage, drivenRightLegVoltage) != 0) return false;
        return Float.compare(that.referenceVoltage, referenceVoltage) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (drivenRightLegVoltage != +0.0f ? Float.floatToIntBits(drivenRightLegVoltage) : 0);
        result = 31 * result + (referenceVoltage != +0.0f ? Float.floatToIntBits(referenceVoltage) : 0);
        return result;
    }
}
