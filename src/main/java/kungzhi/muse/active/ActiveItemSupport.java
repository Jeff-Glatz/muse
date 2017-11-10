package kungzhi.muse.active;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class ActiveItemSupport<Item extends ActiveItem<Item> & Serializable>
        implements ActiveItemListener<Item> {
    private Logger log = LoggerFactory.getLogger(getClass());

    public interface ErrorHandler<Item extends ActiveItem<Item> & Serializable> {
        void error(ActiveItemListener<Item> listener, Exception ex);
    }

    private final List<ActiveItemListener<Item>> listeners = new ArrayList<>();
    private ErrorHandler<Item> errorHandler;

    public ActiveItemSupport() {
        this((listener, ex) -> {
        });
    }

    public ActiveItemSupport(ErrorHandler<Item> errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void addActiveItemListener(ActiveItemListener<Item> listener) {
        this.listeners.add(listener);
    }

    public void removeActiveItemListener(ActiveItemListener<Item> listener) {
        this.listeners.remove(listener);
    }

    public void modified(final Item current, final Item previous) {
        listeners.stream()
                .forEachOrdered(listener -> {
                    try {
                        listener.modified(current, previous);
                    } catch (Exception ex) {
                        log.error(format("Failure notifying listener: %s",
                                listener.getClass().getSimpleName()), ex);
                        errorHandler.error(listener, ex);
                    }
                });
    }
}
