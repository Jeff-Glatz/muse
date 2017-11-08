package kungzhi.muse.osc;

import de.sciss.net.OSCClient;
import de.sciss.net.OSCPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static de.sciss.net.OSCClient.newUsing;

/**
 * OSC messages will be emitted over OSC to paths:
 * <p>
 * /muse/eeg
 * /muse/eeg/dropped_samples
 * /muse/eeg/quantization
 * <p>
 * /muse/acc
 * /muse/acc/dropped_samples
 * <p>
 * /muse/elements/raw_fft0
 * /muse/elements/raw_fft1
 * /muse/elements/raw_fft2
 * /muse/elements/raw_fft3
 * /muse/elements/low_freqs_absolute
 * /muse/elements/delta_absolute
 * /muse/elements/theta_absolute
 * /muse/elements/alpha_absolute
 * /muse/elements/beta_absolute
 * /muse/elements/gamma_absolute
 * /muse/elements/delta_relative
 * /muse/elements/theta_relative
 * /muse/elements/alpha_relative
 * /muse/elements/beta_relative
 * /muse/elements/gamma_relative
 * /muse/elements/delta_session_score
 * /muse/elements/theta_session_score
 * /muse/elements/alpha_session_score
 * /muse/elements/beta_session_score
 * /muse/elements/gamma_session_score
 * /muse/elements/touching_forehead
 * /muse/elements/horseshoe
 * /muse/elements/is_good
 * /muse/elements/blink
 * /muse/elements/jaw_clench
 * <p>
 * /muse/elements/experimental/concentration
 * /muse/elements/experimental/mellow
 * <p>
 * /muse/batt
 * /muse/drlref
 * /muse/config
 * /muse/version
 * /muse/annotation
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * to OSC URL:
 * osc.tcp://127.0.0.1:5000
 */
@Component
public class MessageClient {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MessageDispatcher dispatcher;

    private String protocol;
    private InetSocketAddress receiverAddress;
    private InetSocketAddress transmitterAddress;
    private OSCClient client;

    @Autowired
    public MessageClient(MessageDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Autowired
    public MessageClient withProtocol(
            @Value("${muse.osc.protocol:udp}") String protocol) {
        this.protocol = protocol;
        return this;
    }

    public InetSocketAddress getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(InetSocketAddress receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public MessageClient receivingOn(InetSocketAddress receiverAddress) {
        this.receiverAddress = receiverAddress;
        return this;
    }

    public MessageClient receivingOn(int port) {
        return receivingOn(new InetSocketAddress(port));
    }

    public MessageClient receivingOn(String hostname, int port) {
        return receivingOn(new InetSocketAddress(hostname, port));
    }

    @Autowired
    public MessageClient receivingOn(
            @Value("${muse.osc.receiver.host:0.0.0.0}") InetAddress address,
            @Value("${muse.osc.receiver.port:5000}") int port) {
        return receivingOn(new InetSocketAddress(address, port));
    }

    public InetSocketAddress getTransmitterAddress() {
        return transmitterAddress;
    }

    public void setTransmitterAddress(InetSocketAddress transmitterAddress) {
        this.transmitterAddress = transmitterAddress;
    }

    public MessageClient transmittingOn(InetSocketAddress transmitterAddress) {
        this.transmitterAddress = transmitterAddress;
        return this;
    }

    public MessageClient transmittingOn(int port) {
        return transmittingOn(new InetSocketAddress(port));
    }

    public MessageClient transmittingOn(String hostname, int port) {
        return transmittingOn(new InetSocketAddress(hostname, port));
    }

    @Autowired
    public MessageClient transmittingOn(
            @Value("${muse.osc.receiver.host:localhost}") InetAddress address,
            @Value("${muse.osc.receiver.port:5000}") int port) {
        return transmittingOn(new InetSocketAddress(address, port));
    }

    public void on()
            throws IOException {
        log.info("starting {} client receiving on: {} and transmitting on: {}...",
                protocol, receiverAddress, transmitterAddress);
        client = newUsing(protocol, receiverAddress);
        client.setTarget(transmitterAddress);
        client.addOSCListener(dispatcher);
        client.start();
        log.info("client started.");
    }

    public void send(OSCPacket packet)
            throws IOException {
        client.send(packet);
    }

    @PreDestroy
    public void off()
            throws IOException {
        if (client != null) {
            log.info("stopping {} client receiving on: {} and transmitting on: {}...",
                    protocol, receiverAddress, transmitterAddress);
            client.stop();
            log.info("client stopped.");
        }
    }
}
