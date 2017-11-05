package kungzhi.muse.osc;

import com.google.protobuf.CodedOutputStream;
import com.jsoniter.JsonIterator;
import org.junit.jupiter.api.Test;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static com.jsoniter.JsonIterator.deserialize;
import static kungzhi.muse.osc.Serializer.MuseVersion.newBuilder;
import static kungzhi.muse.osc.Serializer.MuseVersion.parseFrom;


public class SerializerTest {

    @Test
    public void shouldDeserializeVersion()
            throws Exception {
        Serializer.MuseVersion museVersion = deserialize("{" +
                "\"hardware_version\":\"21.0.0\"," +
                "\"firmware_headset_version\":\"7.8.0\"," +
                "\"firmware_bootloader_version\":\"7.2.10\"," +
                "\"firmware_type\":\"Consumer\"," +
                "\"build_number\":56," +
                "\"protocol_version\":2}", Serializer.MuseVersion.class);

        Serializer.MuseVersion version1 = parseFrom(copyFromUtf8("'{" +
                "\"hardware_version\":\"21.0.0\"," +
                "\"firmware_headset_version\":\"7.8.0\"," +
                "\"firmware_bootloader_version\":\"7.2.10\"," +
                "\"firmware_type\":\"Consumer\"," +
                "\"build_number\":56," +
                "\"protocol_version\":2}'"));
    }
}
