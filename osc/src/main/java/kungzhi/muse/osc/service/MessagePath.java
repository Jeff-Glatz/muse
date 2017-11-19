package kungzhi.muse.osc.service;

import kungzhi.muse.model.Accelerometer;
import kungzhi.muse.model.BandPower;
import kungzhi.muse.model.Battery;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.DrlReference;
import kungzhi.muse.model.Eeg;
import kungzhi.muse.model.Fft;
import kungzhi.muse.model.Model;
import kungzhi.muse.model.SessionScore;
import kungzhi.muse.model.SingleValue;
import kungzhi.muse.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum MessagePath {
    EEG("/muse/eeg", Eeg.class, false),
    EEG_DROPPED_SAMPLES("/muse/eeg/dropped_samples", SingleValue.class, false),
    EEG_QUANTIZATION("/muse/eeg/quantization", SingleValue.class, false),

    ACCELEROMETER("/muse/acc", Accelerometer.class, false),
    ACCELEROMETER_DROPPED_SAMPLES("/muse/acc/dropped_samples", SingleValue.class, false),

    RAW_FFT_0("/muse/elements/raw_fft0", Fft.class, false),
    RAW_FFT_1("/muse/elements/raw_fft1", Fft.class, false),
    RAW_FFT_2("/muse/elements/raw_fft2", Fft.class, false),
    RAW_FFT_3("/muse/elements/raw_fft3", Fft.class, false),

    LOW_FREQUENCY_ABSOLUTE("/muse/elements/low_freqs_absolute", BandPower.class, false),
    DELTA_ABSOLUTE("/muse/elements/delta_absolute", BandPower.class, false),
    THETA_ABSOLUTE("/muse/elements/theta_absolute", BandPower.class, false),
    ALPHA_ABSOLUTE("/muse/elements/alpha_absolute", BandPower.class, false),
    BETA_ABSOLUTE("/muse/elements/beta_absolute", BandPower.class, false),
    GAMMA_ABSOLUTE("/muse/elements/gamma_absolute", BandPower.class, false),

    DELTA_RELATIVE("/muse/elements/delta_relative", BandPower.class, false),
    THETA_RELATIVE("/muse/elements/theta_relative", BandPower.class, false),
    ALPHA_RELATIVE("/muse/elements/alpha_relative", BandPower.class, false),
    BETA_RELATIVE("/muse/elements/beta_relative", BandPower.class, false),
    GAMMA_RELATIVE("/muse/elements/gamma_relative", BandPower.class, false),

    DELTA_SESSION_SCORE("/muse/elements/delta_session_score", SessionScore.class, false),
    THETA_SESSION_SCORE("/muse/elements/theta_session_score", SessionScore.class, false),
    ALPHA_SESSION_SCORE("/muse/elements/alpha_session_score", SessionScore.class, false),
    BETA_SESSION_SCORE("/muse/elements/beta_session_score", SessionScore.class, false),
    GAMMA_SESSION_SCORE("/muse/elements/gamma_session_score", SessionScore.class, false),

    HEADBAND_ON("/muse/elements/touching_forehead", SingleValue.class, false),
    HEADBAND_STATUS("/muse/elements/horseshoe", SingleValue.class, false),
    HEADBAND_STATUS_STRICT("/muse/elements/is_good", SingleValue.class, false),

    BLINK("/muse/elements/blink", SingleValue.class, false),
    JAW_CLENCH("/muse/elements/jaw_clench", SingleValue.class, false),

    CONCENTRATION("/muse/elements/experimental/concentration", SingleValue.class, true),
    MELLOW("/muse/elements/experimental/mellow", SingleValue.class, true),

    BATTERY("/muse/batt", Battery.class, false),
    DRL_REFERENCE("/muse/drlref", DrlReference.class, false),
    CONFIGURATION("/muse/config", Configuration.class, false),
    VERSION("/muse/version", Version.class, false),
    ANNOTATION("/muse/annotation", Model.class, false);

    private static final Logger log = LoggerFactory.getLogger(MessagePath.class);
    private static final Map<String, MessagePath> pathMap = mapPaths();

    private final String name;
    private final Class<? extends Model> type;
    private final boolean experimental;

    MessagePath(String name, Class<? extends Model> type, boolean experimental) {
        this.name = name.intern();
        this.type = type;
        this.experimental = experimental;
    }

    public String getName() {
        return name;
    }

    public <M extends Model> Class<M> getType() {
        return (Class<M>) type;
    }

    public boolean isExperimental() {
        return experimental;
    }

    public static MessagePath fromPath(String path) {
        MessagePath metadata = pathMap.get(path);
        if (metadata == null) {
            log.warn("Missing path for {}", path);
        }
        return metadata;
    }

    private static Map<String, MessagePath> mapPaths() {
        return stream(values())
                .collect(toMap(metadata -> metadata.name, identity()));
    }
}
