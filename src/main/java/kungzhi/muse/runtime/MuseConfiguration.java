package kungzhi.muse.runtime;

import kungzhi.muse.model.EmptyModelStream;
import kungzhi.muse.model.Fft;
import kungzhi.muse.osc.service.MessageDispatcher;
import kungzhi.muse.osc.service.MissingTransformerException;
import kungzhi.muse.osc.transform.AccelerometerTransformer;
import kungzhi.muse.osc.transform.BandPowerTransformer;
import kungzhi.muse.osc.transform.BatteryTransformer;
import kungzhi.muse.osc.transform.ConfigurationTransformer;
import kungzhi.muse.osc.transform.DrlReferenceTransformer;
import kungzhi.muse.osc.transform.EegTransformer;
import kungzhi.muse.osc.transform.EmptyMessageTransformer;
import kungzhi.muse.osc.transform.FftTransformer;
import kungzhi.muse.osc.transform.HeadbandStatusStrictTransformer;
import kungzhi.muse.osc.transform.HeadbandStatusTransformer;
import kungzhi.muse.osc.transform.SessionScoreTransformer;
import kungzhi.muse.osc.transform.SingleValueTransformer;
import kungzhi.muse.osc.transform.VersionTransformer;
import kungzhi.muse.platform.BluetoothConfiguration;
import kungzhi.muse.repository.Bands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Clock;
import java.util.concurrent.ExecutorService;

import static java.lang.Integer.parseInt;
import static java.lang.Runtime.getRuntime;
import static java.net.InetAddress.getByName;
import static java.util.concurrent.Executors.newFixedThreadPool;

@ComponentScan({
        "kungzhi.muse.model",
        "kungzhi.muse.osc",
        "kungzhi.muse.repository",
        "kungzhi.muse.runtime",
        "kungzhi.muse.ui"
})
@Import({BluetoothConfiguration.class})
@EnableScheduling
@EnableAutoConfiguration
@SpringBootApplication
public class MuseConfiguration {
    private final Logger log = LoggerFactory.getLogger(MuseConfiguration.class);

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService executorService() {
        return newFixedThreadPool(getRuntime()
                .availableProcessors());
    }

    @Bean
    public ConversionService conversionService() {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(String.class, InetAddress.class, source -> {
            try {
                return getByName(source);
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException(e);
            }
        });
        return service;
    }

    @Bean
    public MessageDispatcher messageDispatcher(Clock clock, MuseHeadband headband, Bands bands) {
        return new MessageDispatcher(clock, headband)
                .streaming("/muse/config",
                        new ConfigurationTransformer(),
                        headband.configurationStream())
                .streaming("/muse/version",
                        new VersionTransformer(),
                        headband.versionStream())
                .streaming("/muse/drlref",
                        new DrlReferenceTransformer(),
                        headband.drlReferenceStream())
                .streaming("/muse/batt",
                        new BatteryTransformer(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/horseshoe",
                        new HeadbandStatusTransformer(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/is_good",
                        new HeadbandStatusStrictTransformer(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/touching_forehead",
                        new SingleValueTransformer<>(Integer.class),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/blink",
                        new SingleValueTransformer<>(Integer.class),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/jaw_clench",
                        new SingleValueTransformer<>(Integer.class),
                        new EmptyModelStream<>())
                .streaming("/muse/acc",
                        new AccelerometerTransformer(),
                        new EmptyModelStream<>())
                .streaming("/muse/acc/dropped_samples",
                        new SingleValueTransformer<>(Integer.class),
                        new EmptyModelStream<>())
                .streaming("/muse/eeg",
                        new EegTransformer(),
                        new EmptyModelStream<>())
                .streaming("/muse/eeg/dropped_samples",
                        new SingleValueTransformer<>(Integer.class),
                        new EmptyModelStream<>())
                .streaming("/muse/eeg/quantization",
                        new SingleValueTransformer<>(Integer.class),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/low_freqs_absolute",
                        new BandPowerTransformer(bands.load("low"), false),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/delta_absolute",
                        new BandPowerTransformer(bands.load("delta"), false),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/theta_absolute",
                        new BandPowerTransformer(bands.load("theta"), false),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/alpha_absolute",
                        new BandPowerTransformer(bands.load("alpha"), false),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/beta_absolute",
                        new BandPowerTransformer(bands.load("beta"), false),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/gamma_absolute",
                        new BandPowerTransformer(bands.load("gamma"), false),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/delta_relative",
                        new BandPowerTransformer(bands.load("delta"), true),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/theta_relative",
                        new BandPowerTransformer(bands.load("theta"), true),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/alpha_relative",
                        new BandPowerTransformer(bands.load("alpha"), true),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/beta_relative",
                        new BandPowerTransformer(bands.load("beta"), true),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/gamma_relative",
                        new BandPowerTransformer(bands.load("gamma"), true),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/delta_session_score",
                        new SessionScoreTransformer(bands.load("delta")),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/theta_session_score",
                        new SessionScoreTransformer(bands.load("theta")),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/alpha_session_score",
                        new SessionScoreTransformer(bands.load("alpha")),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/beta_session_score",
                        new SessionScoreTransformer(bands.load("beta")),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/gamma_session_score",
                        new SessionScoreTransformer(bands.load("gamma")),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/experimental/concentration",
                        new SingleValueTransformer<>(Float.class),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/experimental/mellow",
                        new SingleValueTransformer<>(Float.class),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/raw_fft0",
                        new FftTransformer(0),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/raw_fft1",
                        new FftTransformer(1),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/raw_fft2",
                        new FftTransformer(2),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/raw_fft3",
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
