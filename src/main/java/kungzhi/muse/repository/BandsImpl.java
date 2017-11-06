package kungzhi.muse.repository;

import kungzhi.muse.model.Band;
import kungzhi.muse.model.FrequencyRange;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class BandsImpl
        implements Bands {
    private final Map<String, Band> bands = new HashMap<>();

    public BandsImpl withBand(String identifier, String lowerFrequency, String upperFrequency) {
        bands.put(identifier,
                new Band(identifier,
                        new FrequencyRange(lowerFrequency, upperFrequency)));
        return this;
    }

    public BandsImpl withStandardBands() {
        return withBand("gamma", "30.0", "44.0").
                withBand("beta", "13.0", "30.0").
                withBand("alpha", "7.5", "13.0").
                withBand("theta", "4.0", "8.0").
                withBand("delta", "1.0", "4.0").
                withBand("low", "2.5", "6.1");
    }

    @Override
    public Band band(String name) {
        Band band = bands.get(name);
        if (band == null) {
            throw new IllegalArgumentException(format("Band %s is not defined", name));
        }
        return band;
    }
}
