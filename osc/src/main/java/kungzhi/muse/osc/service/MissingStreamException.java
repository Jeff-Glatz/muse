package kungzhi.muse.osc.service;

public class MissingStreamException
        extends MessageHandlingException {

    public MissingStreamException(String message, String path) {
        super(message, path);
    }

}
