package kungzhi.muse.osc.service;

public class MessageHandlingException
        extends RuntimeException {
    protected final String address;

    public MessageHandlingException(String message, String address) {
        super(message);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
