package kungzhi.muse.platform;

public class MuseHeadbandException
        extends RuntimeException {
    private final MuseHeadband headband;

    public MuseHeadbandException(MuseHeadband headband) {
        this.headband = headband;
    }

    public MuseHeadbandException(MuseHeadband headband, String message) {
        super(message);
        this.headband = headband;
    }

    public MuseHeadbandException(MuseHeadband headband, String message, Throwable cause) {
        super(message, cause);
        this.headband = headband;
    }

    public MuseHeadbandException(MuseHeadband headband, Throwable cause) {
        super(cause);
        this.headband = headband;
    }

    public MuseHeadbandException(MuseHeadband headband, String message, Throwable cause,
                                 boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.headband = headband;
    }

    public MuseHeadband getHeadband() {
        return headband;
    }
}
