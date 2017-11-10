package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Array;

import static java.lang.Float.parseFloat;
import static java.lang.String.format;
import static java.util.Arrays.stream;

@Component
@ManagedResource
public class MuseSimulator {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ConversionService conversionService;
    private final MessageClient client;

    @Autowired
    public MuseSimulator(ConversionService conversionService, MessageClient client) {
        this.conversionService = conversionService;
        this.client = client;
    }

    @ManagedOperation
    public void send(String path, String message)
            throws IOException {
        client.send(new OSCMessage(path, new String[]{message}));
    }

    @ManagedOperation
    public void send(String path, Float[] args)
            throws IOException {
        client.send(new OSCMessage(path, args));
    }

    @ManagedOperation
    public void send(String path, Integer[] args)
            throws IOException {
        client.send(new OSCMessage(path, args));
    }

    @ManagedOperation
    public void send(String path, String[] args)
            throws IOException {
        client.send(new OSCMessage(path, args));
    }

    @ManagedOperation
    public void sendTypedArray(String path, String type, String args)
            throws Exception {
        Class componentType = Class.forName(type);
        client.send(new OSCMessage(path,
                stream(args.split("\\s*,\\s*"))
                        .map(arg -> conversionService.convert(arg, componentType))
                        .toArray(length -> (Object[]) Array.newInstance(componentType, length))));
    }

    @ManagedOperation
    public void sendConfiguration(String eegChannelLayout)
            throws IOException {
        send("/muse/config", format("{\"eeg_channel_layout\":\"%s\"}", eegChannelLayout));
    }

    @ManagedOperation
    public void sendVersion(String buildNumber, String hardwareVersion, String firmwareType,
                            String firmwareHeadsetVersion, String firmwareBootloaderVersion,
                            String protocolVersion)
            throws IOException {
        send("/muse/version", format("{" +
                        "\"build_number\":\"%s\"," +
                        "\"hardware_version\":\"%s\"," +
                        "\"firmware_type\":\"%s\"," +
                        "\"firmware_headset_version\":\"%s\"," +
                        "\"firmware_bootloader_version\":\"%s\"," +
                        "\"protocol_version\":\"%s\"" +
                        "}",
                buildNumber, hardwareVersion, firmwareType,
                firmwareHeadsetVersion, firmwareBootloaderVersion,
                protocolVersion));
    }

    @ManagedOperation
    public void sendBattery(Integer stateOfCharge, Integer fuelGuageVoltage,
                            Integer adcVoltage, Integer temperature)
            throws IOException {
        send("/muse/batt", new Integer[]{stateOfCharge, fuelGuageVoltage, adcVoltage, temperature});
    }

    @ManagedOperation
    public void sendDrlReference(String drl, String ref)
            throws IOException {
        send("/muse/drlref", new Float[]{parseFloat(drl), parseFloat(ref)});
    }
}
