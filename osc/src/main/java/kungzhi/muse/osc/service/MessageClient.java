package kungzhi.muse.osc.service;

import de.sciss.net.OSCPacket;
import de.sciss.net.OSCServer;
import de.sciss.net.OSCTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

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
 * <p>
 * The default configuration of this {@link MessageClient} is setup to
 * receive OSC messages from MuseIO using it's default settings.
 * <p>
 * The client can be altered programmatically by setting the
 * transmitting and receiving addresses or by specifying the following
 * system properties:
 * <p>
 * <table>
 *     <th>
 *         <td>System Property</td>
 *         <td>Default Value</td>
 *     </th>
 *     <tr>
 *         <td>muse.osc.protocol</td>
 *         <td>tcp</td>
 *     </tr>
 *     <tr>
 *         <td>muse.osc.receiver.host</td>
 *         <td>0.0.0.0</td>
 *     </tr>
 *     <tr>
 *         <td>muse.osc.receiver.port</td>
 *         <td>5000</td>
 *     </tr>
 *     <tr>
 *         <td>muse.osc.transmitter.host</td>
 *         <td>localhost</td>
 *     </tr>
 *     <tr>
 *         <td>muse.osc.transmitter.port</td>
 *         <td>5000</td>
 *     </tr>
 * </table>
 */
@Component
public class MessageClient {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MessageDispatcher dispatcher;

    private String protocol;
    private InetSocketAddress receiverAddress;
    private OSCServer receiver;
    private InetSocketAddress transmitterAddress;
    private OSCTransmitter transmitter;

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
            @Value("${muse.osc.protocol:tcp}") String protocol) {
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

    @Autowired
    public MessageClient receivingOn(
            @Value("${muse.osc.receiver.host:0.0.0.0}") String hostname,
            @Value("${muse.osc.receiver.port:5000}") int port) {
        return receivingOn(new InetSocketAddress(hostname, port));
    }

    public MessageClient receivingOn(InetAddress address, int port) {
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

    @Autowired
    public MessageClient transmittingOn(
            @Value("${muse.osc.transmitter.host:localhost}") String hostname,
            @Value("${muse.osc.transmitter.port:5000}") int port) {
        return transmittingOn(new InetSocketAddress(hostname, port));
    }

    public MessageClient transmittingOn(InetAddress address, int port) {
        return transmittingOn(new InetSocketAddress(address, port));
    }

    /**
     * This method is intentionally not annotated with @PostConstruct to
     * allow the application to determine when it is ready to start streaming
     * data from the Muse headband.
     *
     * @throws IOException If an error occurs while turning on the receiving and
     *                     transmitting components
     */
    public void on()
            throws IOException {
        turnOnReceiver();
        turnOnTransmitter();
    }

    public void send(OSCPacket packet)
            throws IOException {
        if (transmitter == null) {
            log.warn("cannot send packet, client is off");
        }
        transmitter.send(packet);
    }

    @PreDestroy
    public void off()
            throws IOException {
        turnOffTransmitter();
        turnOffReceiver();
    }

    private void turnOnTransmitter()
            throws IOException {
        log.info("connecting transmitter to: {}://{}...",
                protocol, transmitterAddress);
        transmitter = OSCTransmitter.newUsing(protocol);
        transmitter.setTarget(transmitterAddress);
        transmitter.connect();
        log.info("transmitter started.");
    }

    private void turnOffTransmitter() {
        if (transmitter != null) {
            log.info("stopping {} transmitter on: {}...", protocol, transmitterAddress);
            transmitter.dispose();
            transmitter = null;
            log.info("transmitter stopped.");
        }
    }

    private void turnOnReceiver()
            throws IOException {
        log.info("starting receiver on: {}://{}...",
                protocol, receiverAddress);
        receiver = OSCServer.newUsing(protocol, receiverAddress);
        receiver.addOSCListener(dispatcher);
        receiver.start();
        log.info("receiver started.");
    }

    private void turnOffReceiver() {
        if (receiver != null) {
            log.info("stopping {} receiver on: {}...", protocol, receiverAddress);
            receiver.dispose();
            receiver = null;
            log.info("receiver stopped.");
        }
    }
}
