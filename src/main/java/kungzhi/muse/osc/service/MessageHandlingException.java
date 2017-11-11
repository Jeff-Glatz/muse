package kungzhi.muse.osc.service;

public class MessageHandlingException extends RuntimeException {
    protected final String path;

    public MessageHandlingException(String message, String path) {
        super(message);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
