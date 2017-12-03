package kungzhi.muse.osc.service;

public class MissingStreamException
        extends MessageHandlingException {

    public MissingStreamException(String message, String address) {
        super(message, address);
    }
}
