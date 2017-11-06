package kungzhi.muse.model;

public enum Bands {
    GAMMA(new Band("Gamma", new FrequencyRange("30.0", "44.0"))),
    BETA(new Band("Beta", new FrequencyRange("13.0", "30.0"))),
    ALPHA(new Band("Alpha", new FrequencyRange("7.5", "13.0"))),
    THETA(new Band("Theta", new FrequencyRange("4.0", "8.0"))),
    DELTA(new Band("Delta", new FrequencyRange("1.0", "4.0"))),
    LOW(new Band("Low", new FrequencyRange("2.5", "6.1")));


    private final Band band;

    Bands(Band band) {
        this.band = band;
    }

    public Band getBand() {
        return band;
    }
}
