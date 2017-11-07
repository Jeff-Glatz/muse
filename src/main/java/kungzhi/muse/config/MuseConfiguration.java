package kungzhi.muse.config;

import kungzhi.muse.osc.BandPowerTransformer;
import kungzhi.muse.osc.BatteryTransformer;
import kungzhi.muse.osc.ConfigurationTransformer;
import kungzhi.muse.osc.DrlReferenceTransformer;
import kungzhi.muse.osc.EegTransformer;
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
                        (session, model) -> {
                        })
                .streaming("/muse/elements/low_freqs_absolute",
                        new BandPowerTransformer(bands.load("low"), false),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/delta_absolute",
                        new BandPowerTransformer(bands.load("delta"), false),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/theta_absolute",
                        new BandPowerTransformer(bands.load("theta"), false),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/alpha_absolute",
                        new BandPowerTransformer(bands.load("alpha"), false),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/beta_absolute",
                        new BandPowerTransformer(bands.load("beta"), false),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/gamma_absolute",
                        new BandPowerTransformer(bands.load("gamma"), false),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/delta_relative",
                        new BandPowerTransformer(bands.load("delta"), true),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/theta_relative",
                        new BandPowerTransformer(bands.load("theta"), true),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/alpha_relative",
                        new BandPowerTransformer(bands.load("alpha"), true),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/beta_relative",
                        new BandPowerTransformer(bands.load("beta"), true),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/gamma_relative",
                        new BandPowerTransformer(bands.load("gamma"), true),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/delta_session_score",
                        new SessionScoreTransformer(bands.load("delta")),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/theta_session_score",
                        new SessionScoreTransformer(bands.load("theta")),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/alpha_session_score",
                        new SessionScoreTransformer(bands.load("alpha")),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/beta_session_score",
                        new SessionScoreTransformer(bands.load("beta")),
                        (session, model) -> {
                        })
                .streaming("/muse/elements/gamma_session_score",
                        new SessionScoreTransformer(bands.load("gamma")),
                        (session, model) -> {
                        })
                // TODO: Unimplemented
                .streaming("/muse/eeg/dropped_samples",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/eeg/quantization",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/acc",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/acc/dropped_samples",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/elements/raw_fft0",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/elements/raw_fft1",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/elements/raw_fft2",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/elements/raw_fft3",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/elements/touching_forehead",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/elements/horseshoe",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/elements/is_good",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/elements/blink",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/elements/jaw_clench",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/elements/experimental/concentration",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/elements/experimental/mellow",
                        (time, message) -> null,
                        (session, model) -> {
                        })
                .streaming("/muse/annotation",
                        (time, message) -> null,
                        (session, model) -> {
                        });
    }
}
