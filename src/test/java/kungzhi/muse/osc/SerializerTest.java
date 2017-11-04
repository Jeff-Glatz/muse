package kungzhi.muse.osc;

import com.google.protobuf.ByteString;
import ix.graphs.io.MuseDataSerializer;
import org.junit.jupiter.api.Test;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static ix.graphs.io.MuseDataSerializer.MuseVersion.parseFrom;
import static kungzhi.muse.osc.Serializer.MuseVersion.newBuilder;


public class SerializerTest {

    @Test
    public void shouldDeserializeVersion()
            throws Exception {
         newBuilder()
                .setHardwareVersion("21.0.0")
                 .setFirmwareHeadsetVersion("7.8.0")
                 .setFirmwareBootloaderVersion("7.2.10")
                 .setFirmwareType("Consumer")
                 .setBuildNumber("56")
                 .setProtocolVersion("2")
                .build()
                .writeTo(System.out);
        MuseDataSerializer.MuseVersion version1 = parseFrom(copyFromUtf8("{" +
                "\"hardware_version\":\"21.0.0\"," +
                "\"firmware_headset_version\":\"7.8.0\"," +
                "\"firmware_bootloader_version\":\"7.2.10\"," +
                "\"firmware_type\":\"Consumer\"," +
                "\"build_number\":56," +
                "\"protocol_version\":2}"));
    }
}
