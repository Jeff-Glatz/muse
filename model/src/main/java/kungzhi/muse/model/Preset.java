package kungzhi.muse.model;

import kungzhi.lang.Localizable;

import java.util.EnumSet;
import java.util.stream.Stream;

import static java.lang.Float.parseFloat;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.EnumSet.copyOf;
import static kungzhi.muse.model.Sensor.AF7;
import static kungzhi.muse.model.Sensor.AF8;
import static kungzhi.muse.model.Sensor.AUX_LEFT;
import static kungzhi.muse.model.Sensor.AUX_RIGHT;
import static kungzhi.muse.model.Sensor.TP10;
import static kungzhi.muse.model.Sensor.TP9;

public enum Preset
        implements Localizable {
    TEN("10", sensors(TP9, AF7, AF8, TP10), 10, "220"),
    TWELVE("12", sensors(TP9, AF7, AF8, TP10), 10, "220"),
    FOURTEEN("14", sensors(TP9, AF7, AF8, TP10), 10, "220"),
    AB("AB", sensors(TP9, AF7, AF8, TP10, AUX_LEFT, AUX_RIGHT), 16, "500"),
    AD("AD", sensors(TP9, AF7, AF8, TP10), 16, "500");

    private final String id;
    private final EnumSet<Sensor> sensors;
    private final int eegBits;
    private final float eegFrequency;

    Preset(String id, EnumSet<Sensor> sensors, Integer eegBits, String eegFrequency) {
        this.id = id.intern();
        this.sensors = sensors;
        this.eegBits = eegBits;
        this.eegFrequency = parseFloat(eegFrequency);
    }

    public String getId() {
        return id;
    }

    public EnumSet<Sensor> getSensors() {
        return sensors;
    }

    public int getEegBits() {
        return eegBits;
    }

    public float getEegFrequency() {
        return eegFrequency;
    }


    public static Preset fromId(String id) {
        String fastString = id.intern();
        return Stream.of(values())
                .filter(preset -> preset.id == fastString)
                .findFirst()
                .orElse(null);
    }

    private static EnumSet<Sensor> sensors(Sensor... sensors) {
        return copyOf(asList(sensors));
    }

    @Override
    public String resourceKey() {
        return format("model.preset.%s", getId());
    }
}
