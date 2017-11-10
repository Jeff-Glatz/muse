package kungzhi.muse.model;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

@Component
public class Configuration
        extends ActiveModel<Configuration> {

    // Global
    private String macAddress;
    private String serialNumber;
    private Preset preset;

    // Network Protocol
    private Boolean compressionEnabled;

    // EEG Data
    private Boolean filtersEnabled;
    private Integer notchFrequencyInHz;
    private Integer eegSampleFrequencyInHz;
    private Integer eegOutputFrequencyInHz;
    private Integer eegChannelCount;
    private final SortedSet<EegChannel> eegChannelLayout = new TreeSet<>();
    private Integer eegSamplesBitWidth;
    private Integer eegDownSample;
    private Float afeGain;

    // DRL/REF
    private Boolean drlRefDataEnabled;
    private Float drlRefConversionFactor;
    private Integer drlRefSampleFrequencyInHz;

    // Accelerometer
    private Boolean accDataEnabled;
    private String accUnits;
    private Integer accSampleFrequencyInHz;

    // Battery
    private Boolean batteryDataEnabled;
    private Integer batteryPercentRemaining;
    private Integer batteryMilliVolts;

    // Error Data
    private Boolean errorDataEnabled;

    public Configuration() {
        super(0);
    }

    public Configuration(long time) {
        super(time);
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Configuration withMacAddress(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Configuration withSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public Preset getPreset() {
        return preset;
    }

    public void setPreset(Preset preset) {
        this.preset = preset;
    }

    public Configuration withPreset(Preset preset) {
        this.preset = preset;
        return this;
    }

    public Boolean isCompressionEnabled() {
        return compressionEnabled;
    }

    public void setCompressionEnabled(Boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
    }

    public Configuration withCompressionEnabled(Boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
        return this;
    }

    public Boolean isFiltersEnabled() {
        return filtersEnabled;
    }

    public void setFiltersEnabled(Boolean filtersEnabled) {
        this.filtersEnabled = filtersEnabled;
    }

    public Configuration withFiltersEnabled(Boolean filtersEnabled) {
        this.filtersEnabled = filtersEnabled;
        return this;
    }

    public Integer getNotchFrequencyInHz() {
        return notchFrequencyInHz;
    }

    public void setNotchFrequencyInHz(Integer notchFrequencyInHz) {
        this.notchFrequencyInHz = notchFrequencyInHz;
    }

    public Configuration withNotchFrequencyInHz(Integer notchFrequencyInHz) {
        this.notchFrequencyInHz = notchFrequencyInHz;
        return this;
    }

    public Integer getEegSampleFrequencyInHz() {
        return eegSampleFrequencyInHz;
    }

    public void setEegSampleFrequencyInHz(Integer eegSampleFrequencyInHz) {
        this.eegSampleFrequencyInHz = eegSampleFrequencyInHz;
    }

    public Configuration withEegSampleFrequencyInHz(Integer eegSampleFrequencyInHz) {
        this.eegSampleFrequencyInHz = eegSampleFrequencyInHz;
        return this;
    }

    public Integer getEegOutputFrequencyInHz() {
        return eegOutputFrequencyInHz;
    }

    public void setEegOutputFrequencyInHz(Integer eegOutputFrequencyInHz) {
        this.eegOutputFrequencyInHz = eegOutputFrequencyInHz;
    }

    public Configuration withEegOutputFrequencyInHz(Integer eegOutputFrequencyInHz) {
        this.eegOutputFrequencyInHz = eegOutputFrequencyInHz;
        return this;
    }

    public Integer getEegChannelCount() {
        return eegChannelCount;
    }

    public void setEegChannelCount(Integer eegChannelCount) {
        this.eegChannelCount = eegChannelCount;
    }

    public Configuration withEegChannelCount(Integer eegChannelCount) {
        this.eegChannelCount = eegChannelCount;
        return this;
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

    public Integer getEegSamplesBitWidth() {
        return eegSamplesBitWidth;
    }

    public void setEegSamplesBitWidth(Integer eegSamplesBitWidth) {
        this.eegSamplesBitWidth = eegSamplesBitWidth;
    }

    public Configuration withEegSamplesBitWidth(Integer eegSamplesBitWidth) {
        this.eegSamplesBitWidth = eegSamplesBitWidth;
        return this;
    }

    public Integer getEegDownSample() {
        return eegDownSample;
    }

    public void setEegDownSample(Integer eegDownSample) {
        this.eegDownSample = eegDownSample;
    }

    public Configuration withEegDownSample(Integer eegDownSample) {
        this.eegDownSample = eegDownSample;
        return this;
    }

    public Float getAfeGain() {
        return afeGain;
    }

    public void setAfeGain(Float afeGain) {
        this.afeGain = afeGain;
    }

    public Configuration withAfeGain(Float afeGain) {
        this.afeGain = afeGain;
        return this;
    }

    public Boolean isDrlRefDataEnabled() {
        return drlRefDataEnabled;
    }

    public void setDrlRefDataEnabled(Boolean drlRefDataEnabled) {
        this.drlRefDataEnabled = drlRefDataEnabled;
    }

    public Configuration withDrlRefDataEnabled(Boolean drlRefDataEnabled) {
        this.drlRefDataEnabled = drlRefDataEnabled;
        return this;
    }

    public Float getDrlRefConversionFactor() {
        return drlRefConversionFactor;
    }

    public void setDrlRefConversionFactor(Float drlRefConversionFactor) {
        this.drlRefConversionFactor = drlRefConversionFactor;
    }

    public Configuration withDrlRefConversionFactor(Float drlRefConversionFactor) {
        this.drlRefConversionFactor = drlRefConversionFactor;
        return this;
    }

    public Integer getDrlRefSampleFrequencyInHz() {
        return drlRefSampleFrequencyInHz;
    }

    public void setDrlRefSampleFrequencyInHz(Integer drlRefSampleFrequencyInHz) {
        this.drlRefSampleFrequencyInHz = drlRefSampleFrequencyInHz;
    }

    public Configuration withDrlRefSampleFrequencyInHz(Integer drlRefSampleFrequencyInHz) {
        this.drlRefSampleFrequencyInHz = drlRefSampleFrequencyInHz;
        return this;
    }

    public Boolean isAccDataEnabled() {
        return accDataEnabled;
    }

    public void setAccDataEnabled(Boolean accDataEnabled) {
        this.accDataEnabled = accDataEnabled;
    }

    public Configuration withAccDataEnabled(Boolean accDataEnabled) {
        this.accDataEnabled = accDataEnabled;
        return this;
    }

    public String getAccUnits() {
        return accUnits;
    }

    public void setAccUnits(String accUnits) {
        this.accUnits = accUnits;
    }

    public Configuration withAccUnits(String accUnits) {
        this.accUnits = accUnits;
        return this;
    }

    public Integer getAccSampleFrequencyInHz() {
        return accSampleFrequencyInHz;
    }

    public void setAccSampleFrequencyInHz(Integer accSampleFrequencyInHz) {
        this.accSampleFrequencyInHz = accSampleFrequencyInHz;
    }

    public Configuration withAccSampleFrequencyInHz(Integer accSampleFrequencyInHz) {
        this.accSampleFrequencyInHz = accSampleFrequencyInHz;
        return this;
    }

    public Boolean isBatteryDataEnabled() {
        return batteryDataEnabled;
    }

    public void setBatteryDataEnabled(Boolean batteryDataEnabled) {
        this.batteryDataEnabled = batteryDataEnabled;
    }

    public Configuration withBatteryDataEnabled(Boolean batteryDataEnabled) {
        this.batteryDataEnabled = batteryDataEnabled;
        return this;
    }

    public Integer getBatteryPercentRemaining() {
        return batteryPercentRemaining;
    }

    public void setBatteryPercentRemaining(Integer batteryPercentRemaining) {
        this.batteryPercentRemaining = batteryPercentRemaining;
    }

    public Configuration withBatteryPercentRemaining(Integer batteryPercentRemaining) {
        this.batteryPercentRemaining = batteryPercentRemaining;
        return this;
    }

    public Integer getBatteryMilliVolts() {
        return batteryMilliVolts;
    }

    public void setBatteryMilliVolts(Integer batteryMilliVolts) {
        this.batteryMilliVolts = batteryMilliVolts;
    }

    public Configuration withBatteryMilliVolts(Integer batteryMilliVolts) {
        this.batteryMilliVolts = batteryMilliVolts;
        return this;
    }

    public Boolean isErrorDataEnabled() {
        return errorDataEnabled;
    }

    public void setErrorDataEnabled(Boolean errorDataEnabled) {
        this.errorDataEnabled = errorDataEnabled;
    }

    public Configuration withErrorDataEnabled(Boolean errorDataEnabled) {
        this.errorDataEnabled = errorDataEnabled;
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
        result = 31 * result + (macAddress != null ? macAddress.hashCode() : 0);
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        result = 31 * result + (preset != null ? preset.hashCode() : 0);
        result = 31 * result + (compressionEnabled != null ? compressionEnabled.hashCode() : 0);
        result = 31 * result + (filtersEnabled != null ? filtersEnabled.hashCode() : 0);
        result = 31 * result + (notchFrequencyInHz != null ? notchFrequencyInHz.hashCode() : 0);
        result = 31 * result + (eegSampleFrequencyInHz != null ? eegSampleFrequencyInHz.hashCode() : 0);
        result = 31 * result + (eegOutputFrequencyInHz != null ? eegOutputFrequencyInHz.hashCode() : 0);
        result = 31 * result + (eegChannelCount != null ? eegChannelCount.hashCode() : 0);
        result = 31 * result + (eegSamplesBitWidth != null ? eegSamplesBitWidth.hashCode() : 0);
        result = 31 * result + (eegDownSample != null ? eegDownSample.hashCode() : 0);
        result = 31 * result + (afeGain != null ? afeGain.hashCode() : 0);
        result = 31 * result + (drlRefDataEnabled != null ? drlRefDataEnabled.hashCode() : 0);
        result = 31 * result + (drlRefConversionFactor != null ? drlRefConversionFactor.hashCode() : 0);
        result = 31 * result + (drlRefSampleFrequencyInHz != null ? drlRefSampleFrequencyInHz.hashCode() : 0);
        result = 31 * result + (accDataEnabled != null ? accDataEnabled.hashCode() : 0);
        result = 31 * result + (accUnits != null ? accUnits.hashCode() : 0);
        result = 31 * result + (accSampleFrequencyInHz != null ? accSampleFrequencyInHz.hashCode() : 0);
        result = 31 * result + (batteryDataEnabled != null ? batteryDataEnabled.hashCode() : 0);
        result = 31 * result + (batteryPercentRemaining != null ? batteryPercentRemaining.hashCode() : 0);
        result = 31 * result + (batteryMilliVolts != null ? batteryMilliVolts.hashCode() : 0);
        result = 31 * result + (errorDataEnabled != null ? errorDataEnabled.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                ", time=" + time +
                ", eegChannelLayout=" + eegChannelLayout +
                ", macAddress='" + macAddress + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", preset=" + preset +
                ", compressionEnabled=" + compressionEnabled +
                ", filtersEnabled=" + filtersEnabled +
                ", notchFrequencyInHz=" + notchFrequencyInHz +
                ", eegSampleFrequencyInHz=" + eegSampleFrequencyInHz +
                ", eegOutputFrequencyInHz=" + eegOutputFrequencyInHz +
                ", eegChannelCount=" + eegChannelCount +
                ", eegSamplesBitWidth=" + eegSamplesBitWidth +
                ", eegDownSample=" + eegDownSample +
                ", afeGain=" + afeGain +
                ", drlRefDataEnabled=" + drlRefDataEnabled +
                ", drlRefConversionFactor=" + drlRefConversionFactor +
                ", drlRefSampleFrequencyInHz=" + drlRefSampleFrequencyInHz +
                ", accDataEnabled=" + accDataEnabled +
                ", accUnits='" + accUnits + '\'' +
                ", accSampleFrequencyInHz=" + accSampleFrequencyInHz +
                ", batteryDataEnabled=" + batteryDataEnabled +
                ", batteryPercentRemaining=" + batteryPercentRemaining +
                ", batteryMilliVolts=" + batteryMilliVolts +
                ", errorDataEnabled=" + errorDataEnabled +
                '}';
    }

    @Override
    protected Configuration newInstance() {
        return new Configuration();
    }

    protected Configuration update(Configuration that) {
        this.time = that.time;
        this.eegChannelLayout.clear();
        this.eegChannelLayout.addAll(that.eegChannelLayout);
        this.macAddress = that.macAddress;
        this.serialNumber = that.serialNumber;
        this.preset = that.preset;
        this.compressionEnabled = that.compressionEnabled;
        this.filtersEnabled = that.filtersEnabled;
        this.notchFrequencyInHz = that.notchFrequencyInHz;
        this.eegSampleFrequencyInHz = that.eegSampleFrequencyInHz;
        this.eegOutputFrequencyInHz = that.eegOutputFrequencyInHz;
        this.eegChannelCount = that.eegChannelCount;
        this.eegSamplesBitWidth = that.eegSamplesBitWidth;
        this.eegDownSample = that.eegDownSample;
        this.afeGain = that.afeGain;
        this.drlRefDataEnabled = that.drlRefDataEnabled;
        this.drlRefConversionFactor = that.drlRefConversionFactor;
        this.drlRefSampleFrequencyInHz = that.drlRefSampleFrequencyInHz;
        this.accDataEnabled = that.accDataEnabled;
        this.accUnits = that.accUnits;
        this.accSampleFrequencyInHz = that.accSampleFrequencyInHz;
        this.batteryDataEnabled = that.batteryDataEnabled;
        this.batteryPercentRemaining = that.batteryPercentRemaining;
        this.batteryMilliVolts = that.batteryMilliVolts;
        this.errorDataEnabled = that.errorDataEnabled;
        return this;
    }

    private boolean sameAs(Configuration that) {
        if (!eegChannelLayout.equals(that.eegChannelLayout))
            return false;
        if (macAddress != null ? !macAddress.equals(that.macAddress) : that.macAddress != null) return false;
        if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
        if (preset != that.preset) return false;
        if (compressionEnabled != null ? !compressionEnabled.equals(that.compressionEnabled) : that.compressionEnabled != null)
            return false;
        if (filtersEnabled != null ? !filtersEnabled.equals(that.filtersEnabled) : that.filtersEnabled != null)
            return false;
        if (notchFrequencyInHz != null ? !notchFrequencyInHz.equals(that.notchFrequencyInHz) : that.notchFrequencyInHz != null)
            return false;
        if (eegSampleFrequencyInHz != null ? !eegSampleFrequencyInHz.equals(that.eegSampleFrequencyInHz) : that.eegSampleFrequencyInHz != null)
            return false;
        if (eegOutputFrequencyInHz != null ? !eegOutputFrequencyInHz.equals(that.eegOutputFrequencyInHz) : that.eegOutputFrequencyInHz != null)
            return false;
        if (eegChannelCount != null ? !eegChannelCount.equals(that.eegChannelCount) : that.eegChannelCount != null)
            return false;
        if (eegSamplesBitWidth != null ? !eegSamplesBitWidth.equals(that.eegSamplesBitWidth) : that.eegSamplesBitWidth != null)
            return false;
        if (eegDownSample != null ? !eegDownSample.equals(that.eegDownSample) : that.eegDownSample != null)
            return false;
        if (afeGain != null ? !afeGain.equals(that.afeGain) : that.afeGain != null) return false;
        if (drlRefDataEnabled != null ? !drlRefDataEnabled.equals(that.drlRefDataEnabled) : that.drlRefDataEnabled != null)
            return false;
        if (drlRefConversionFactor != null ? !drlRefConversionFactor.equals(that.drlRefConversionFactor) : that.drlRefConversionFactor != null)
            return false;
        if (drlRefSampleFrequencyInHz != null ? !drlRefSampleFrequencyInHz.equals(that.drlRefSampleFrequencyInHz) : that.drlRefSampleFrequencyInHz != null)
            return false;
        if (accDataEnabled != null ? !accDataEnabled.equals(that.accDataEnabled) : that.accDataEnabled != null)
            return false;
        if (accUnits != null ? !accUnits.equals(that.accUnits) : that.accUnits != null) return false;
        if (accSampleFrequencyInHz != null ? !accSampleFrequencyInHz.equals(that.accSampleFrequencyInHz) : that.accSampleFrequencyInHz != null)
            return false;
        if (batteryDataEnabled != null ? !batteryDataEnabled.equals(that.batteryDataEnabled) : that.batteryDataEnabled != null)
            return false;
        if (batteryPercentRemaining != null ? !batteryPercentRemaining.equals(that.batteryPercentRemaining) : that.batteryPercentRemaining != null)
            return false;
        if (batteryMilliVolts != null ? !batteryMilliVolts.equals(that.batteryMilliVolts) : that.batteryMilliVolts != null)
            return false;
        return errorDataEnabled != null ? errorDataEnabled.equals(that.errorDataEnabled) : that.errorDataEnabled == null;
    }
}
