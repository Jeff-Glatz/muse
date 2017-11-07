package kungzhi.muse.config;

import kungzhi.muse.model.EmptyModelStream;
import kungzhi.muse.osc.BandPowerTransformer;
import kungzhi.muse.osc.BatteryTransformer;
import kungzhi.muse.osc.ConfigurationTransformer;
import kungzhi.muse.osc.DrlReferenceTransformer;
import kungzhi.muse.osc.EegTransformer;
import kungzhi.muse.osc.EmptyMessageTransformer;
import kungzhi.muse.osc.MessageDispatcher;
import kungzhi.muse.osc.SessionScoreTransformer;
import kungzhi.muse.osc.VersionTransformer;
import kungzhi.muse.repository.Bands;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

import static java.lang.Runtime.getRuntime;
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

    @Bean(destroyMethod = "shutdown")
    public ExecutorService executorService() {
        return newFixedThreadPool(getRuntime()
                .availableProcessors());
    }

    @Bean
    public MessageDispatcher messageDispatcher(MuseSession museSession, Bands bands) {
        return new MessageDispatcher(museSession)
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
                .streaming("/muse/eeg",
                        new EegTransformer(),
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
                // TODO: Unimplemented
                .streaming("/muse/eeg/dropped_samples",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/eeg/quantization",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/acc",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/acc/dropped_samples",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/raw_fft0",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/raw_fft1",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/raw_fft2",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/raw_fft3",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/touching_forehead",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/horseshoe",
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
                .streaming("/muse/elements/experimental/concentration",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/elements/experimental/mellow",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>())
                .streaming("/muse/annotation",
                        new EmptyMessageTransformer<>(),
                        new EmptyModelStream<>());
    }
}
