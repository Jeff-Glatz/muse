package kungzhi.muse.osc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum Path {
    EEG("/muse/eeg", false),
    EEG_DROPPED_SAMPLES("/muse/eeg/dropped_samples", false),
    EEG_QUANTIZATION("/muse/eeg/quantization", false),

    ACCELEROMETER("/muse/acc", false),
    ACCELEROMETER_DROPPED_SAMPLES("/muse/acc/dropped_samples", false),

    RAW_FFT_0("/muse/elements/raw_fft0", false),
    RAW_FFT_1("/muse/elements/raw_fft1", false),
    RAW_FFT_2("/muse/elements/raw_fft2", false),
    RAW_FFT_3("/muse/elements/raw_fft3", false),

    LOW_FREQUENCY_ABSOLUTE("/muse/elements/low_freqs_absolute", false),
    DELTA_ABSOLUTE("/muse/elements/delta_absolute", false),
    THETA_ABSOLUTE("/muse/elements/theta_absolute", false),
    ALPHA_ABSOLUTE("/muse/elements/alpha_absolute", false),
    BETA_ABSOLUTE("/muse/elements/beta_absolute", false),
    GAMMA_ABSOLUTE("/muse/elements/gamma_absolute", false),

    DELTA_RELATIVE("/muse/elements/delta_relative", false),
    THETA_RELATIVE("/muse/elements/theta_relative", false),
    ALPHA_RELATIVE("/muse/elements/alpha_relative", false),
    BETA_RELATIVE("/muse/elements/beta_relative", false),
    GAMMA_RELATIVE("/muse/elements/gamma_relative", false),

    DELTA_SESSION_SCORE("/muse/elements/delta_session_score", false),
    THETA_SESSION_SCORE("/muse/elements/theta_session_score", false),
    ALPHA_SESSION_SCORE("/muse/elements/alpha_session_score", false),
    BETA_SESSION_SCORE("/muse/elements/beta_session_score", false),
    GAMMA_SESSION_SCORE("/muse/elements/gamma_session_score", false),

    HEADBAND_ON("/muse/elements/touching_forehead", false),
    HEADBAND_STATUS("/muse/elements/horseshoe", false),
    HEADBAND_STATUS_STRICT("/muse/elements/is_good", false),

    BLINK("/muse/elements/blink", false),
    JAW_CLENCH("/muse/elements/jaw_clench", false),

    CONCENTRATION("/muse/elements/experimental/concentration", true),
    MELLOW("/muse/elements/experimental/mellow", true),

    BATTERY("/muse/batt", false),
    DRL_REFERENCE("/muse/drlref", false),
    CONFIGURATION("/muse/config", false),
    VERSION("/muse/version", false),
    ANNOTATION("/muse/annotation", false);

    private static final Logger log = LoggerFactory.getLogger(Path.class);
    private static final Map<String, Path> pathMap = mapPaths();

    private final String path;
    private final boolean experimental;

    Path(String path, boolean experimental) {
        this.path = path.intern();
        this.experimental = experimental;
    }

    public String getPath() {
        return path;
    }

    public boolean isExperimental() {
        return experimental;
    }

    public static Path fromPath(String path) {
        Path metadata = pathMap.get(path);
        if (metadata == null) {
            log.warn("Missing path for {}", path);
        }
        return metadata;
    }

    private static Map<String, Path> mapPaths() {
        return stream(values())
                .collect(toMap(metadata -> metadata.path, identity()));
    }
}
