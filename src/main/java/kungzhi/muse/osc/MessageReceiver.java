package kungzhi.muse.osc;

import de.sciss.net.OSCServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;

import static de.sciss.net.OSCServer.newUsing;

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
public class MessageReceiver {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MessageDispatcher dispatcher;

    private String protocol;
    private InetSocketAddress address;
    private OSCServer server;

    public MessageReceiver(MessageDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public MessageReceiver withProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public MessageReceiver onAddress(InetSocketAddress address) {
        this.address = address;
        return this;
    }

    public MessageReceiver onAddress(int port) {
        return onAddress(new InetSocketAddress(port));
    }

    public MessageReceiver onAddress(String hostname, int port) {
        return onAddress(new InetSocketAddress(hostname, port));
    }

    public void on()
            throws IOException {
        log.info("starting {} receiver on {}...", protocol, address);
        server = newUsing(protocol, address);
        server.addOSCListener(dispatcher);
        server.start();
        log.info("started {} receiver on {}.", protocol, address);
    }

    @PreDestroy
    public void off()
            throws IOException {
        if (server != null) {
            log.info("stopping {} receiver on {}...", protocol, address);
            server.stop();
            log.info("stopped {} receiver on {}.", protocol, address);
        }
    }
}
