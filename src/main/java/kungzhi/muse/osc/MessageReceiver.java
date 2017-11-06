package kungzhi.muse.osc;

import de.sciss.net.OSCServer;
import kungzhi.muse.repository.Bands;
import kungzhi.muse.repository.BandsImpl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

import static de.sciss.net.OSCServer.newUsing;
import static java.lang.Thread.currentThread;

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
public class MessageReceiver {
    private final MessageDispatcher dispatcher;

    private String protocol;
    private int port;
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public MessageReceiver withPort(int port) {
        this.port = port;
        return this;
    }

    @PostConstruct
    public void on()
            throws IOException {
        server = newUsing(protocol, port);
        server.addOSCListener(dispatcher);
        server.start();
    }

    @PreDestroy
    public void off()
            throws IOException {
        server.stop();
    }

    public static void main(String[] args)
            throws Exception {
        Bands bands = new BandsImpl()
                .withStandardBands();
        MessageReceiver receiver = new MessageReceiver(new MessageDispatcher()
                .withTransformer("/muse/elements/low_freqs_absolute",
                        new BandPowerTransformer(bands.band("low"), false))
                .withTransformer("/muse/elements/delta_absolute",
                        new BandPowerTransformer(bands.band("delta"), false))
                .withTransformer("/muse/elements/theta_absolute",
                        new BandPowerTransformer(bands.band("theta"), false))
                .withTransformer("/muse/elements/alpha_absolute",
                        new BandPowerTransformer(bands.band("alpha"), false))
                .withTransformer("/muse/elements/beta_absolute",
                        new BandPowerTransformer(bands.band("beta"), false))
                .withTransformer("/muse/elements/gamma_absolute",
                        new BandPowerTransformer(bands.band("gamma"), false))
                .withTransformer("/muse/elements/delta_relative",
                        new BandPowerTransformer(bands.band("delta"), true))
                .withTransformer("/muse/elements/theta_relative",
                        new BandPowerTransformer(bands.band("theta"), true))
                .withTransformer("/muse/elements/alpha_relative",
                        new BandPowerTransformer(bands.band("alpha"), true))
                .withTransformer("/muse/elements/beta_relative",
                        new BandPowerTransformer(bands.band("beta"), true))
                .withTransformer("/muse/elements/gamma_relative",
                        new BandPowerTransformer(bands.band("gamma"), true)));
        receiver.setProtocol("tcp");
        receiver.setPort(5000);
        try {
            receiver.on();
            currentThread().suspend();
        } finally {
            receiver.off();
        }
    }
}
