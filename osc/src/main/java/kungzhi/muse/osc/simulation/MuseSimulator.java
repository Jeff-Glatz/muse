package kungzhi.muse.osc.simulation;

import com.illposed.osc.OSCMessage;
import kungzhi.muse.osc.service.MessageClient;
import kungzhi.muse.osc.service.MessageDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.ConversionService;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static java.lang.Class.forName;
import static java.lang.Float.parseFloat;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

/**
 * The {@link MuseSimulator} is a JMX managed singleton that can be used to transmit
 * simulated messages from the Muse headband to the {@link MessageDispatcher}.
 * <p>
 * When running locally, simply use the JDK provided {@code jconsole} to connect to
 * the running Java process. From there any invoked MBean operations will result in
 * OSC messages being delivered to the running application.
 *
 * @see MessageDispatcher
 * @see MessageClient
 */
@Component
@Profile("simulation")
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
    public void send(String address, String message)
            throws IOException {
        send(address, singletonList(message));
    }

    @ManagedOperation
    public void sendTypedArray(String address, String type, String args)
            throws Exception {
        send(address, args(forName(type), args));
    }

    @ManagedOperation
    public void sendConfiguration(String mac, String serial, String preset, String eegChannelLayout)
            throws IOException {
        int eegChannelCount = eegChannelLayout.trim().split(" ").length;
        send("/muse/config", format("{" +
                "\"mac_addr\":\"%s\"," +
                "\"serial_number\":\"%s\"," +
                "\"preset\":\"%s\"," +
                "\"eeg_channel_count\":\"%s\"," +
                "\"eeg_channel_layout\":\"%s\"" +
                "}", mac, serial, preset, eegChannelCount, eegChannelLayout));
    }

    @ManagedOperation
    public void sendDefaultConfiguration()
            throws IOException {
        sendConfiguration("AA:BB:CC:DD:EE:00:FF", "Muse-4833", "14", "TP9 AF7 AF8 TP10");
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
        send("/muse/batt", asList(stateOfCharge, fuelGuageVoltage, adcVoltage, temperature));
    }

    @ManagedOperation
    public void sendDrlReference(String drl, String ref)
            throws IOException {
        send("/muse/drlref", asList(parseFloat(drl), parseFloat(ref)));
    }

    @ManagedOperation
    public void sendHeadbandStatus(boolean strict, String args)
            throws IOException {
        send(strict ? "/muse/elements/is_good" : "/muse/elements/horseshoe",
                args(Float.class, args));
    }

    @ManagedOperation
    public void sendIsGood(String args)
            throws IOException {
        send("/muse/elements/is_good", args(Integer.class, args));
    }

    @ManagedOperation
    public void sendBandPower(String band, boolean relative, String args)
            throws IOException {
        send(format("/muse/elements/%s_%s", band, relative ? "relative" : "absolute"),
                args(Float.class, args));
    }

    @ManagedOperation
    public void sendSessionScore(String band, String args)
            throws IOException {
        send(format("/muse/elements/%s_session_score", band),
                args(Float.class, args));
    }

    @ManagedOperation
    public void sendFFT(int channelIndex, String args)
            throws IOException {
        send(format("/muse/elements/raw_fft%s", channelIndex),
                args(Float.class, args));
    }

    @ManagedOperation
    public void sendConcentration(Float value)
            throws IOException {
        send("/muse/elements/experimental/concentration", singletonList(value));
    }

    @ManagedOperation
    public void sendMellow(Float value)
            throws IOException {
        send("/muse/elements/experimental/mellow", singletonList(value));
    }

    @ManagedOperation
    public void sendTouchingForehead(Integer value)
            throws IOException {
        send("/muse/elements/touching_forehead", singletonList(value));
    }

    @ManagedOperation
    public void sendBlink(Integer value)
            throws IOException {
        send("/muse/elements/blink", singletonList(value));
    }

    @ManagedOperation
    public void sendJawClench(Integer value)
            throws IOException {
        send("/muse/elements/jaw_clench", singletonList(value));
    }

    private <T> List<T> args(Class<T> componentType, String args) {
        return stream(args.split("\\s*,\\s*"))
                .map(arg -> conversionService.convert(arg, componentType))
                .collect(toList());
    }

    private void send(String address, Collection args)
            throws IOException {
        client.send(new OSCMessage(address, args));
    }
}
