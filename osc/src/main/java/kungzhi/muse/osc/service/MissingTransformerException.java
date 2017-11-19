package kungzhi.muse.osc.service;

public class MissingTransformerException
        extends MessageHandlingException {

    public MissingTransformerException(String message, String path) {
        super(message, path);
    }

}
