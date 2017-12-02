package kungzhi.muse.model;

import kungzhi.muse.lang.Localizable;

import static java.lang.String.format;

public enum Sensor
        implements Localizable {
    TP9, TP10,
    FP1, FP2,
    AF7, AF8,
    AUX_LEFT, AUX_RIGHT,
    DRL, REF;

    @Override
    public String resourceKey() {
        return format("model.sensor.%s", name());
    }
}
