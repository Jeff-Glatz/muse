package kungzhi.muse.model;

import java.util.EventListener;

public interface SessionListener
        extends EventListener {
    void configurationChanged(Configuration previous, Configuration current);
}
