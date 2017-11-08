package kungzhi.muse.model;

import java.util.EnumSet;

import static java.lang.Float.parseFloat;
import static java.util.EnumSet.of;
import static kungzhi.muse.model.Sensor.AF7;
import static kungzhi.muse.model.Sensor.AF8;
import static kungzhi.muse.model.Sensor.AUX_LEFT;
import static kungzhi.muse.model.Sensor.AUX_RIGHT;
import static kungzhi.muse.model.Sensor.TP10;
import static kungzhi.muse.model.Sensor.TP9;

public enum Preset {
    TEN("10", of(TP9, AF7, AF8, TP10), 10, "220"),
    TWELVE("12", of(TP9, AF7, AF8, TP10), 10, "220"),
    FOURTEEN("14", of(TP9, AF7, AF8, TP10), 10, "220"),
    AB("AB", of(TP9, AF7, AF8, TP10, AUX_LEFT, AUX_RIGHT), 16, "500"),
    AD("AD", of(TP9, AF7, AF8, TP10), 16, "500");
    private final String id;
    private final EnumSet<Sensor> sensors;
    private final Integer eegBits;
    private final Float eegFrequency;

    Preset(String id, EnumSet<Sensor> sensors, Integer eegBits, String eegFrequency) {
        this.id = id;
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

    public Integer getEegBits() {
        return eegBits;
    }

    public Float getEegFrequency() {
        return eegFrequency;
    }
}
