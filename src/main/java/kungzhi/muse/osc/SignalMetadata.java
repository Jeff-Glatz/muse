package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

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
public enum SignalMetadata {
    EEG("/muse/eeg", false, Eeg.class),
    EEG_DROPPED_SAMPLES("/muse/eeg/dropped_samples", false, EegDroppedSamples.class),
    EEG_QUANTIZATION("/muse/eeg/quantization", false, EegQuantization.class),

    ACCELEROMETER("/muse/acc", false, Accelerometer.class),
    ACCELEROMETER_DROPPED_SAMPLES("/muse/acc/dropped_samples", false, AccelerometerDroppedSamples.class),

    RAW_FFT_0("/muse/elements/raw_fft0", false, RawFft0.class),
    RAW_FFT_1("/muse/elements/raw_fft1", false, RawFft1.class),
    RAW_FFT_2("/muse/elements/raw_fft2", false, RawFft2.class),
    RAW_FFT_3("/muse/elements/raw_fft3", false, RawFft3.class),

    LOW_FREQUENCY_ABSOLUTE("/muse/elements/low_freqs_absolute", false, LowFrequencyAbsolute.class),
    DELTA_ABSOLUTE("/muse/elements/delta_absolute", false, DeltaAbsolute.class),
    THETA_ABSOLUTE("/muse/elements/theta_absolute", false, ThetaAbsolute.class),
    ALPHA_ABSOLUTE("/muse/elements/alpha_absolute", false, AlphaAbsolute.class),
    BETA_ABSOLUTE("/muse/elements/beta_absolute", false, BetaAbsolute.class),
    GAMMA_ABSOLUTE("/muse/elements/gamma_absolute", false, GammaAbsolute.class),

    DELTA_RELATIVE("/muse/elements/delta_relative", false, DeltaRelative.class),
    THETA_RELATIVE("/muse/elements/theta_relative", false, ThetaRelative.class),
    ALPHA_RELATIVE("/muse/elements/alpha_relative", false, AlphaRelative.class),
    BETA_RELATIVE("/muse/elements/beta_relative", false, BetaRelative.class),
    GAMMA_RELATIVE("/muse/elements/gamma_relative", false, GammaRelative.class),

    DELTA_SESSION_SCORE("/muse/elements/delta_session_score", false, DeltaSessionScore.class),
    THETA_SESSION_SCORE("/muse/elements/theta_session_score", false, ThetaSessionScore.class),
    ALPHA_SESSION_SCORE("/muse/elements/alpha_session_score", false, AlphaSessionScore.class),
    BETA_SESSION_SCORE("/muse/elements/beta_session_score", false, BetaSessionScore.class),
    GAMMA_SESSION_SCORE("/muse/elements/gamma_session_score", false, GammaSessionScore.class),

    HEADBAND_ON("/muse/elements/touching_forehead", false, HeadbandOn.class),
    HEADBAND_STATUS("/muse/elements/horseshoe", false, HeadbandStatus.class, new HeadbandStatusFactory()),
    HEADBAND_STATUS_STRICT("/muse/elements/is_good", false, HeadbandStatusStrict.class),

    BLINK("/muse/elements/blink", false, Blink.class),
    JAW_CLENCH("/muse/elements/jaw_clench", false, JawClench.class),

    CONCENTRATION("/muse/elements/experimental/concentration", true, SimpleSignal.class, new SimpleSignalFactory()),
    MELLOW("/muse/elements/experimental/mellow", true, SimpleSignal.class, new SimpleSignalFactory()),

    BATTERY("/muse/batt", false, Battery.class, new BatteryFactory()),
    DRL_REFERENCE("/muse/drlref", false, DrlReference.class, new DrlReferenceFactory()),
    CONFIGURATION("/muse/config", false, Configuration.class, new ConfigurationFactory()),
    VERSION("/muse/version", false, Version.class, new VersionFactory()),
    ANNOTATION("/muse/annotation", false, Annotation.class, new AnnotationFactory());

    private static final Logger log = LoggerFactory.getLogger(SignalMetadata.class);
    private static final Map<String, SignalMetadata> pathToSignalMap = buildPathToSignalMap();

    private final String path;
    private final boolean experimental;
    private final Class<? extends Signal> type;
    private final SignalFactory<? extends Signal> factory;

    <S extends Signal> SignalMetadata(String path, boolean experimental, Class<S> type, SignalFactory<S> factory) {
        this.path = path.intern();
        this.experimental = experimental;
        this.type = type;
        this.factory = factory;
    }

    <S extends Signal> SignalMetadata(String path, boolean experimental, Class<S> type) {
        this(path, experimental, type, new GenericSignalFactory<>(type));
    }

    public String getPath() {
        return path;
    }

    public boolean isExperimental() {
        return experimental;
    }

    public Class<? extends Signal> getType() {
        return type;
    }

    public <S extends Signal> S create(OSCMessage message)
            throws Exception {
        return (S) factory.create(message);
    }

    public static SignalMetadata fromPath(String path) {
        SignalMetadata metadata = pathToSignalMap.get(path);
        if (metadata == null) {
            log.warn("Missing metadata for {}", path);
        }
        return metadata;
    }

    public static  <S extends Signal> S as(Signal signal) {
        return (S) signal;
    }

    private static Map<String, SignalMetadata> buildPathToSignalMap() {
        return stream(values())
                .collect(toMap(metadata -> metadata.path, identity()));
    }
}
