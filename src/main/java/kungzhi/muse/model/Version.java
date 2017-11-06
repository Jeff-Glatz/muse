package kungzhi.muse.model;

public class Version
        extends AbstractModel {

    private String buildNumber;
    private String firmwareType;
    private String hardwareVersion;
    private String firmwareHeadsetVersion;
    private String firmwareBootloaderVersion;
    private String protocolVersion;

    public Version(long time) {
        super(time);
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public Version withBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
        return this;
    }

    public String getFirmwareType() {
        return firmwareType;
    }

    public void setFirmwareType(String firmwareType) {
        this.firmwareType = firmwareType;
    }

    public Version withFirmwareType(String firmwareType) {
        this.firmwareType = firmwareType;
        return this;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public Version withHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
        return this;
    }

    public String getFirmwareHeadsetVersion() {
        return firmwareHeadsetVersion;
    }

    public void setFirmwareHeadsetVersion(String firmwareHeadsetVersion) {
        this.firmwareHeadsetVersion = firmwareHeadsetVersion;
    }

    public Version withFirmwareHeadsetVersion(String firmwareHeadsetVersion) {
        this.firmwareHeadsetVersion = firmwareHeadsetVersion;
        return this;
    }

    public String getFirmwareBootloaderVersion() {
        return firmwareBootloaderVersion;
    }

    public void setFirmwareBootloaderVersion(String firmwareBootloaderVersion) {
        this.firmwareBootloaderVersion = firmwareBootloaderVersion;
    }

    public Version withFirmwareBootloaderVersion(String firmwareBootloaderVersion) {
        this.firmwareBootloaderVersion = firmwareBootloaderVersion;
        return this;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public Version withProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Version version = (Version) o;

        if (buildNumber != null ? !buildNumber.equals(version.buildNumber) : version.buildNumber != null) return false;
        if (firmwareType != null ? !firmwareType.equals(version.firmwareType) : version.firmwareType != null)
            return false;
        if (hardwareVersion != null ? !hardwareVersion.equals(version.hardwareVersion) : version.hardwareVersion != null)
            return false;
        if (firmwareHeadsetVersion != null ? !firmwareHeadsetVersion.equals(version.firmwareHeadsetVersion) : version.firmwareHeadsetVersion != null)
            return false;
        if (protocolVersion != null ? !protocolVersion.equals(version.protocolVersion) : version.protocolVersion != null)
            return false;
        return firmwareBootloaderVersion != null ? firmwareBootloaderVersion.equals(version.firmwareBootloaderVersion) : version.firmwareBootloaderVersion == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (buildNumber != null ? buildNumber.hashCode() : 0);
        result = 31 * result + (firmwareType != null ? firmwareType.hashCode() : 0);
        result = 31 * result + (hardwareVersion != null ? hardwareVersion.hashCode() : 0);
        result = 31 * result + (firmwareHeadsetVersion != null ? firmwareHeadsetVersion.hashCode() : 0);
        result = 31 * result + (protocolVersion != null ? protocolVersion.hashCode() : 0);
        result = 31 * result + (firmwareBootloaderVersion != null ? firmwareBootloaderVersion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Version{" +
                "time=" + time +
                ", buildNumber='" + buildNumber + '\'' +
                ", firmwareType='" + firmwareType + '\'' +
                ", hardwareVersion='" + hardwareVersion + '\'' +
                ", firmwareHeadsetVersion='" + firmwareHeadsetVersion + '\'' +
                ", firmwareBootloaderVersion='" + firmwareBootloaderVersion + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                '}';
    }
}
