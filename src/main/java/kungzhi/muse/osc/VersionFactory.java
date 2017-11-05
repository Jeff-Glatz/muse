package kungzhi.muse.osc;

import com.jsoniter.any.Any;

import java.io.IOException;

import static kungzhi.muse.osc.Serializer.MuseVersion.newBuilder;

public class VersionFactory
        extends JsonSignalFactory<Version> {

    @Override
    protected Version create(String path, Any json)
            throws IOException {
        return new Version(path, newBuilder()
                .setBuildNumber(json.toString("build_number"))
                .setFirmwareType(json.toString("firmware_type"))
                .setHardwareVersion(json.toString("hardware_version"))
                .setFirmwareHeadsetVersion(json.toString("firmware_headset_version"))
                .setProtocolVersion(json.toString("protocol_version"))
                .setFirmwareBootloaderVersion(json.toString("firmware_bootloader_version"))
                .build());
    }
}
