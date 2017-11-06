package kungzhi.muse.osc;

import com.jsoniter.any.Any;
import kungzhi.muse.osc.SignalSerializer.MuseVersion;

import java.io.IOException;

import static kungzhi.muse.osc.SignalSerializer.MuseVersion.newBuilder;

public class VersionTransformer
        extends JsonMuseSignalTransformer<MuseVersion> {

    @Override
    protected MuseSignal<MuseVersion> fromOsc(String path, Any json)
            throws IOException {
        return new MuseSignal<>(path, newBuilder()
                .setBuildNumber(json.toString("build_number"))
                .setFirmwareType(json.toString("firmware_type"))
                .setHardwareVersion(json.toString("hardware_version"))
                .setFirmwareHeadsetVersion(json.toString("firmware_headset_version"))
                .setProtocolVersion(json.toString("protocol_version"))
                .setFirmwareBootloaderVersion(json.toString("firmware_bootloader_version"))
                .build());
    }
}
