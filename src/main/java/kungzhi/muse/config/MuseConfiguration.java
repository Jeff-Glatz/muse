package kungzhi.muse.config;

import kungzhi.muse.model.EmptyModelStream;
import kungzhi.muse.osc.AccelerometerTransformer;
import kungzhi.muse.osc.BandPowerTransformer;
import kungzhi.muse.osc.BatteryTransformer;
import kungzhi.muse.osc.ConfigurationTransformer;
import kungzhi.muse.osc.DrlReferenceTransformer;
import kungzhi.muse.osc.EegTransformer;
import kungzhi.muse.osc.EmptyMessageTransformer;
import kungzhi.muse.osc.FftTransformer;
import kungzhi.muse.osc.HorseshoeTransformer;
import kungzhi.muse.osc.MessageDispatcher;
import kungzhi.muse.osc.SessionScoreTransformer;
import kungzhi.muse.osc.SingleValueTransformer;
import kungzhi.muse.osc.VersionTransformer;
import kungzhi.muse.repository.Bands;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Clock;
import java.util.concurrent.ExecutorService;

import static java.lang.Runtime.getRuntime;
import static java.net.InetAddress.getByName;
import static java.util.concurrent.Executors.newFixedThreadPool;

@Configuration
@ComponentScan({
        "kungzhi.muse.config",
        "kungzhi.muse.model",
        "kungzhi.muse.osc",
        "kungzhi.muse.repository"
})
@EnableAutoConfiguration
public class MuseConfiguration {

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
    public MessageDispatcher messageDispatcher(Clock clock, MuseSession museSession, Bands bands) {
        return new MessageDispatcher(clock, museSession)
                .streaming("/muse/config",
                        new ConfigurationTransformer(),
                        museSession.configurationStream())
                .streaming("/muse/version",
                        new VersionTransformer(),
                        museSession.versionStream())
                .streaming("/muse/batt",
                        new BatteryTransformer(),
                        museSession.batteryStream())
                .streaming("/muse/drlref",
                        new DrlReferenceTransformer(),
                        museSession.drlReferenceStream())
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
                .streaming("/muse/elements/horseshoe",
                        new HorseshoeTransformer(),
                        new EmptyModelStream<>())
                // TODO: Unimplemented
                .streaming("/muse/elements/touching_forehead",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/is_good",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/blink",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/jaw_clench",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/annotation",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>());
    }
}
