package kungzhi.muse.osc;

public class Battery
        extends AbstractSignal {
    private final Integer stateOfCharge;
    private final Integer fuelGuageVoltage;
    private final Integer adcVoltage;
    private final Integer temperature;

    public Battery(String path, Integer stateOfCharge,
                   Integer fuelGuageVoltage, Integer adcVoltage,
                   Integer temperature) {
        super(path);
        this.stateOfCharge = stateOfCharge;
        this.fuelGuageVoltage = fuelGuageVoltage;
        this.adcVoltage = adcVoltage;
        this.temperature = temperature;
    }

    public Integer getStateOfCharge() {
        return stateOfCharge;
    }

    public Float getPercentRemaining() {
        return stateOfCharge / 100f;
    }

    public Integer getFuelGuageVoltage() {
        return fuelGuageVoltage;
    }

    public Integer getAdcVoltage() {
        return adcVoltage;
    }

    public Integer getTemperature() {
        return temperature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Battery battery = (Battery) o;

        if (stateOfCharge != null ? !stateOfCharge.equals(battery.stateOfCharge) : battery.stateOfCharge != null)
            return false;
        if (fuelGuageVoltage != null ? !fuelGuageVoltage.equals(battery.fuelGuageVoltage) : battery.fuelGuageVoltage != null)
            return false;
        if (adcVoltage != null ? !adcVoltage.equals(battery.adcVoltage) : battery.adcVoltage != null) return false;
        return temperature != null ? temperature.equals(battery.temperature) : battery.temperature == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (stateOfCharge != null ? stateOfCharge.hashCode() : 0);
        result = 31 * result + (fuelGuageVoltage != null ? fuelGuageVoltage.hashCode() : 0);
        result = 31 * result + (adcVoltage != null ? adcVoltage.hashCode() : 0);
        result = 31 * result + (temperature != null ? temperature.hashCode() : 0);
        return result;
    }
}
