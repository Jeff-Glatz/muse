package kungzhi.muse.osc;

import de.sciss.net.OSCMessage;

import java.lang.reflect.Constructor;

public class GenericSignalFactory<S extends Signal>
        implements SignalFactory<S> {
    private final Class<S> type;

    public GenericSignalFactory(Class<S> type) {
        this.type = type;
    }

    @Override
    public S create(OSCMessage message)
            throws Exception {
        Constructor<S> constructor = type.getConstructor(String.class);
        return constructor.newInstance(message.getName());
    }
}
