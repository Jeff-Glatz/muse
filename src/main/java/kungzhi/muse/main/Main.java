package kungzhi.muse.main;

import de.sciss.net.OSCServer;
import kungzhi.muse.osc.Battery;
import kungzhi.muse.osc.Signal;
import kungzhi.muse.osc.SignalMetadata;
import kungzhi.muse.osc.SignalSerializer.MuseVersion;
import kungzhi.muse.osc.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static de.sciss.net.OSCPacket.printTextOn;
import static kungzhi.muse.osc.SignalMetadata.*;

/**
 * OSC messages will be emitted over OSC to paths:
 * /muse/eeg
 * /muse/eeg/quantization
 * /muse/acc
 * /muse/version
 * /muse/config
 * /muse/batt
 * /muse/drlref
 * /muse/elements
 * <p>
 * to OSC URL:
 * osc.tcp://127.0.0.1:5000
 */
public class Main {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Set<String> paths = new HashSet<>();

    public void listen(String protocol, int port)
            throws IOException {
        paths.clear();
        OSCServer server = OSCServer.newUsing(protocol, port);
        server.addOSCListener((message, sender, time) -> {
            try {
                SignalMetadata metadata = fromPath(message.getName());
                if (metadata == null) {
                    return;
                }
                collectPath(metadata);
//                if (metadata.isExperimental() ||
//                        metadata == BATTERY) {
                    printTextOn(System.out, message);
//                }
//                Signal signal = metadata.create(message);
//                switch (metadata) {
//                    case BATTERY:
//                        printBatteryStatus(as(signal));
//                        break;
//                    case VERSION:
//                        printVersion(as(signal));
//                }
            } catch (Exception e) {
                log.error("Failure processing OSC message", e);
            }
        });
        server.start();
    }

    private void collectPath(SignalMetadata metadata) {
        if (paths.add(metadata.getPath())) {
            StringBuffer buffer = new StringBuffer();
            paths.forEach(channel -> {
                if (buffer.length() > 0) {
                    buffer.append("\n");
                }
                buffer.append(channel);
            });
//            System.out.printf("**** Channels[%d] ****\n%s\n", paths.size(), buffer);
        }
    }

    private void printBatteryStatus(Battery battery) {
        log.info(new StringBuilder("\n")
                        .append("state of charge: {}\n")
                        .append("percent remaining: {}%\n")
                        .append("fuel gauge voltage: {}\n")
                        .append("adc voltage: {}\n")
                        .append("temperature: {}\n").toString(),
                battery.getStateOfCharge(),
                battery.getPercentRemaining(),
                battery.getFuelGuageVoltage(),
                battery.getAdcVoltage(),
                battery.getTemperature());
    }

    private void printVersion(Version version) {
        MuseVersion delegate = version.getDelegate();
        log.info(new StringBuilder("\n")
                        .append("build number: {}\n")
                        .append("firmware type: {}\n")
                        .append("hardware version: {}\n")
                        .append("firmware headset version: {}\n")
                        .append("protocol version: {}\n")
                        .append("firmware bootloader version: {}\n").toString(),
                delegate.getBuildNumber(),
                delegate.getFirmwareType(),
                delegate.getHardwareVersion(),
                delegate.getFirmwareHeadsetVersion(),
                delegate.getProtocolVersion(),
                delegate.getFirmwareBootloaderVersion());
    }

    public static void main(String[] args)
            throws Exception {
        Main main = new Main();
        main.listen("tcp", 5000);
        Thread.currentThread().suspend();
    }
}
