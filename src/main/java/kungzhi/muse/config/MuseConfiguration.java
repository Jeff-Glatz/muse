package kungzhi.muse.config;

import kungzhi.muse.osc.*;
import kungzhi.muse.repository.Bands;
import kungzhi.muse.stream.ConfigurationStream;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        "kungzhi.muse.model",
        "kungzhi.muse.osc",
        "kungzhi.muse.repository",
        "kungzhi.muse.stream"
})
@EnableAutoConfiguration
public class MuseConfiguration {

    @Bean
    public MessageDispatcher messageDispatcher(Bands bands) {
        return new MessageDispatcher()
                .streaming("/muse/config",
                        new ConfigurationTransformer(),
                        new ConfigurationStream())
                .streaming("/muse/version",
                        new VersionTransformer(),
                        (session, model) -> {
                        })
                .streaming("/muse/batt",
                        new BatteryTransformer(),
                        (session, model) -> {
                        })
                .streaming("/muse/drlref",
                        new DrlReferenceTransformer(),
                        (session, model) -> {
                        })
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
