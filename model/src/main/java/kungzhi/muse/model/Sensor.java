package kungzhi.muse.model;

import kungzhi.muse.lang.Localizable;

public enum Sensor
        implements Localizable {
    TP9("sensor.TP9.name"), TP10("sensor.TP10.name"),
    FP1("sensor.FP1.name"), FP2("sensor.FP2.name"),
    AF7("sensor.AF7.name"), AF8("sensor.AF8.name"),
    AUX_LEFT("sensor.aux.left.name"), AUX_RIGHT("sensor.aux.right.name"),
    DRL("sensor.meta.DRL.name"), REF("sensor.meta.REF.name");

    private final String resourceKey;

    Sensor(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    @Override
    public String resourceKey() {
        return resourceKey;
    }
}
