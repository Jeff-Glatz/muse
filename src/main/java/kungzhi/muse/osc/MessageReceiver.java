package kungzhi.muse.osc;

import de.sciss.net.OSCServer;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.repository.Bands;
import kungzhi.muse.repository.BandsImpl;
import kungzhi.muse.stream.ConfigurationStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final Logger log = LoggerFactory.getLogger(getClass());
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

    public void on()
            throws IOException {
        log.info("starting {} receiver on port {}...", protocol, port);
        server = newUsing(protocol, port);
        server.addOSCListener(dispatcher);
        server.start();
        log.info("started {} receiver on port {}.", protocol, port);
    }

    public void off()
            throws IOException {
        server.stop();
    }

    public static void main(String[] args)
            throws Exception {
        Bands bands = new BandsImpl()
                .withStandardBands();
        MessageReceiver receiver = new MessageReceiver(new MessageDispatcher(new Configuration())
                .withHandler("/muse/config",
                        new ConfigurationTransformer(),
                        new ConfigurationStream())
                .withHandler("/muse/version",
                        new VersionTransformer(),
                        (session, model) -> {
                        })
                .withHandler("/muse/batt",
                        new BatteryTransformer(),
                        (session, model) -> {
                        })
                .withHandler("/muse/drlref",
                        new DrlReferenceTransformer(),
                        (session, model) -> {
                        })
                .withHandler("/muse/eeg",
                        new EegTransformer(),
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/low_freqs_absolute",
                        new BandPowerTransformer(bands.load("low"), false),
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/delta_absolute",
                        new BandPowerTransformer(bands.load("delta"), false),
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/theta_absolute",
                        new BandPowerTransformer(bands.load("theta"), false),
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/alpha_absolute",
                        new BandPowerTransformer(bands.load("alpha"), false),
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/beta_absolute",
                        new BandPowerTransformer(bands.load("beta"), false),
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/gamma_absolute",
                        new BandPowerTransformer(bands.load("gamma"), false),
                        (session, model) -> {
                            System.out.println("G:" + model.average());
                        })
                .withHandler("/muse/elements/delta_relative",
                        new BandPowerTransformer(bands.load("delta"), true),
                        (session, model) -> {
                            System.out.println("D:" + model.average());
                        })
                .withHandler("/muse/elements/theta_relative",
                        new BandPowerTransformer(bands.load("theta"), true),
                        (session, model) -> {
                            System.out.println("T:" + model.average());

                        })
                .withHandler("/muse/elements/alpha_relative",
                        new BandPowerTransformer(bands.load("alpha"), true),
                        (session, model) -> {
                            System.out.println("A:" + model.average());
                        })
                .withHandler("/muse/elements/beta_relative",
                        new BandPowerTransformer(bands.load("beta"), true),
                        (session, model) -> {
                            System.out.println("B:" + model.average());
                        })
                .withHandler("/muse/elements/gamma_relative",
                        new BandPowerTransformer(bands.load("gamma"), true),
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/delta_session_score",
                        new SessionScoreTransformer(bands.load("delta")),
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/theta_session_score",
                        new SessionScoreTransformer(bands.load("theta")),
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/alpha_session_score",
                        new SessionScoreTransformer(bands.load("alpha")),
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/beta_session_score",
                        new SessionScoreTransformer(bands.load("beta")),
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/gamma_session_score",
                        new SessionScoreTransformer(bands.load("gamma")),
                        (session, model) -> {
                        })
                // TODO: Unimplemented
                .withHandler("/muse/eeg/dropped_samples",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/eeg/quantization",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/acc",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/acc/dropped_samples",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/raw_fft0",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/raw_fft1",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/raw_fft2",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/raw_fft3",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/touching_forehead",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/horseshoe",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/is_good",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/blink",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/jaw_clench",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/experimental/concentration",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/elements/experimental/mellow",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .withHandler("/muse/annotation",
                        (time, message) -> null,
                        (session, model) -> {
                        }));
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
