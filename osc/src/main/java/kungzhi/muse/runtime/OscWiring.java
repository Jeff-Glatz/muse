package kungzhi.muse.runtime;

import kungzhi.muse.model.EmptyModelStream;
import kungzhi.muse.model.Fft;
import kungzhi.muse.model.Headband;
import kungzhi.muse.osc.service.MessageDispatcher;
import kungzhi.muse.osc.service.MissingTransformerException;
import kungzhi.muse.osc.transform.BandPowerTransformer;
import kungzhi.muse.osc.transform.EmptyMessageTransformer;
import kungzhi.muse.osc.transform.FftTransformer;
import kungzhi.muse.osc.transform.SessionScoreTransformer;
import kungzhi.muse.osc.transform.SingleValueTransformer;
import kungzhi.muse.platform.MuseIO;
import kungzhi.muse.repository.Bands;
import kungzhi.muse.runtime.OscProperties.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Clock;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.Integer.parseInt;
import static java.net.InetAddress.getLocalHost;
import static kungzhi.muse.osc.service.MessagePath.ACCELEROMETER_DROPPED_SAMPLES;
import static kungzhi.muse.osc.service.MessagePath.ALPHA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.ALPHA_RELATIVE;
import static kungzhi.muse.osc.service.MessagePath.ALPHA_SESSION_SCORE;
import static kungzhi.muse.osc.service.MessagePath.BATTERY;
import static kungzhi.muse.osc.service.MessagePath.BETA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.BETA_RELATIVE;
import static kungzhi.muse.osc.service.MessagePath.BETA_SESSION_SCORE;
import static kungzhi.muse.osc.service.MessagePath.BLINK;
import static kungzhi.muse.osc.service.MessagePath.CONCENTRATION;
import static kungzhi.muse.osc.service.MessagePath.CONFIGURATION;
import static kungzhi.muse.osc.service.MessagePath.DELTA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.DELTA_RELATIVE;
import static kungzhi.muse.osc.service.MessagePath.DELTA_SESSION_SCORE;
import static kungzhi.muse.osc.service.MessagePath.DRL_REFERENCE;
import static kungzhi.muse.osc.service.MessagePath.EEG_DROPPED_SAMPLES;
import static kungzhi.muse.osc.service.MessagePath.EEG_QUANTIZATION;
import static kungzhi.muse.osc.service.MessagePath.GAMMA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.GAMMA_RELATIVE;
import static kungzhi.muse.osc.service.MessagePath.GAMMA_SESSION_SCORE;
import static kungzhi.muse.osc.service.MessagePath.HEADBAND_STATUS;
import static kungzhi.muse.osc.service.MessagePath.HEADBAND_TOUCHING;
import static kungzhi.muse.osc.service.MessagePath.JAW_CLENCH;
import static kungzhi.muse.osc.service.MessagePath.LOW_FREQUENCY_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.MELLOW;
import static kungzhi.muse.osc.service.MessagePath.RAW_FFT_0;
import static kungzhi.muse.osc.service.MessagePath.RAW_FFT_1;
import static kungzhi.muse.osc.service.MessagePath.RAW_FFT_2;
import static kungzhi.muse.osc.service.MessagePath.RAW_FFT_3;
import static kungzhi.muse.osc.service.MessagePath.THETA_ABSOLUTE;
import static kungzhi.muse.osc.service.MessagePath.THETA_RELATIVE;
import static kungzhi.muse.osc.service.MessagePath.THETA_SESSION_SCORE;
import static kungzhi.muse.osc.service.MessagePath.VERSION;

@ComponentScan({"kungzhi.muse.osc"})
@Import(ModelWiring.class)
@Configuration
@EnableConfigurationProperties(OscProperties.class)
public class OscWiring {
    private final Logger log = LoggerFactory.getLogger(OscWiring.class);
    private final OscProperties properties;

    public OscWiring(OscProperties properties) {
        this.properties = properties;
    }

    @Bean
    public MuseIO museIO()
            throws Exception {
        Service receiver = properties.getReceiver();
        return new MuseIO()
                .withProtocol(properties.protocol("tcp"))
                .withHost(receiver.host(getLocalHost()))
                .withPort(receiver.port(5000));
    }

    @Bean
    public MessageDispatcher messageDispatcher(Clock clock, ScheduledExecutorService executor,
                                               Headband headband, Bands bands) {
        return new MessageDispatcher(clock, executor, headband)
                .withStream(BATTERY, headband.batteryStream())
                .withStream(CONFIGURATION, headband.configurationStream())
                .withStream(DRL_REFERENCE, headband.drlReferenceStream())
                .withStream(VERSION, headband.versionStream())
                .withStream(HEADBAND_STATUS, headband.statusStream())
                .withStream(HEADBAND_TOUCHING, headband.touchingStream())
                .streaming(BLINK,
                        new SingleValueTransformer<>(Integer.class),
                        new EmptyModelStream<>())
                .streaming(JAW_CLENCH,
                        new SingleValueTransformer<>(Integer.class),
                        new EmptyModelStream<>())
                .streaming(ACCELEROMETER_DROPPED_SAMPLES,
                        new SingleValueTransformer<>(Integer.class),
                        new EmptyModelStream<>())
                .streaming(EEG_DROPPED_SAMPLES,
                        new SingleValueTransformer<>(Integer.class),
                        new EmptyModelStream<>())
                .streaming(EEG_QUANTIZATION,
                        new SingleValueTransformer<>(Integer.class),
                        new EmptyModelStream<>())
                .streaming(LOW_FREQUENCY_ABSOLUTE,
                        new BandPowerTransformer(bands.load("low"), false),
                        new EmptyModelStream<>())
                .streaming(DELTA_ABSOLUTE,
                        new BandPowerTransformer(bands.load("delta"), false),
                        new EmptyModelStream<>())
                .streaming(THETA_ABSOLUTE,
                        new BandPowerTransformer(bands.load("theta"), false),
                        new EmptyModelStream<>())
                .streaming(ALPHA_ABSOLUTE,
                        new BandPowerTransformer(bands.load("alpha"), false),
                        new EmptyModelStream<>())
                .streaming(BETA_ABSOLUTE,
                        new BandPowerTransformer(bands.load("beta"), false),
                        new EmptyModelStream<>())
                .streaming(GAMMA_ABSOLUTE,
                        new BandPowerTransformer(bands.load("gamma"), false),
                        new EmptyModelStream<>())
                .streaming(DELTA_RELATIVE,
                        new BandPowerTransformer(bands.load("delta"), true),
                        new EmptyModelStream<>())
                .streaming(THETA_RELATIVE,
                        new BandPowerTransformer(bands.load("theta"), true),
                        new EmptyModelStream<>())
                .streaming(ALPHA_RELATIVE,
                        new BandPowerTransformer(bands.load("alpha"), true),
                        new EmptyModelStream<>())
                .streaming(BETA_RELATIVE,
                        new BandPowerTransformer(bands.load("beta"), true),
                        new EmptyModelStream<>())
                .streaming(GAMMA_RELATIVE,
                        new BandPowerTransformer(bands.load("gamma"), true),
                        new EmptyModelStream<>())
                .streaming(DELTA_SESSION_SCORE,
                        new SessionScoreTransformer(bands.load("delta")),
                        new EmptyModelStream<>())
                .streaming(THETA_SESSION_SCORE,
                        new SessionScoreTransformer(bands.load("theta")),
                        new EmptyModelStream<>())
                .streaming(ALPHA_SESSION_SCORE,
                        new SessionScoreTransformer(bands.load("alpha")),
                        new EmptyModelStream<>())
                .streaming(BETA_SESSION_SCORE,
                        new SessionScoreTransformer(bands.load("beta")),
                        new EmptyModelStream<>())
                .streaming(GAMMA_SESSION_SCORE,
                        new SessionScoreTransformer(bands.load("gamma")),
                        new EmptyModelStream<>())
                .streaming(CONCENTRATION,
                        new SingleValueTransformer<>(Float.class),
                        new EmptyModelStream<>())
                .streaming(MELLOW,
                        new SingleValueTransformer<>(Float.class),
                        new EmptyModelStream<>())
                .streaming(RAW_FFT_0,
                        new FftTransformer(0),
                        new EmptyModelStream<>())
                .streaming(RAW_FFT_1,
                        new FftTransformer(1),
                        new EmptyModelStream<>())
                .streaming(RAW_FFT_2,
                        new FftTransformer(2),
                        new EmptyModelStream<>())
                .streaming(RAW_FFT_3,
                        new FftTransformer(3),
                        new EmptyModelStream<>())
                // Error Handling
                .handling(MissingTransformerException.class,
                        (dispatcher, message, error) -> {
                            String path = message.getName();
                            if (path.startsWith("/muse/elements/raw_fft")) {
                                int channelIndex = parseInt(path.substring(
                                        "/muse/elements/raw_fft".length(),
                                        path.length()));
                                log.info("Discovered new raw FFT channel with index: {}", channelIndex);
                                dispatcher.withTransformer(path, Fft.class,
                                        new FftTransformer(channelIndex));
                            }
                        })
                // TODO: Unimplemented
                .streaming("/muse/annotation",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>());
    }
}
