package kungzhi.muse.osc.transform;

import com.jsoniter.any.Any;
import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Version;
import kungzhi.muse.osc.service.Transformer;

import static com.jsoniter.JsonIterator.deserialize;
import static kungzhi.muse.osc.service.MessagePath.VERSION;
import static kungzhi.muse.osc.transform.MessageHelper.argumentAt;

@Transformer(VERSION)
public class VersionTransformer
        implements MessageTransformer<Version> {

    @Override
    public Version fromMessage(long time, OSCMessage message)
            throws Exception {
        Any json = deserialize(argumentAt(message, String.class, 0));
        return new Version(time)
                .withBuildNumber(json.toString("build_number"))
                .withHardwareVersion(json.toString("hardware_version"))
                .withFirmwareType(json.toString("firmware_type"))
                .withFirmwareHeadsetVersion(json.toString("firmware_headset_version"))
                .withFirmwareBootloaderVersion(json.toString("firmware_bootloader_version"))
                .withProtocolVersion(json.toString("protocol_version"));
    }
}
