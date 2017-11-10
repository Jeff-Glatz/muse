package kungzhi.muse.model;

import kungzhi.muse.active.ActiveItem;
import kungzhi.muse.active.ActiveItemListener;
import kungzhi.muse.active.ActiveItemSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ActiveModel<Item extends ActiveItem<Item> & Model>
        extends AbstractModel
        implements ActiveItem<Item> {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final transient ActiveItemSupport<Item> support =
            new ActiveItemSupport<>();

    public ActiveModel(long time) {
        super(time);
    }

    @Override
    public void addActiveItemListener(ActiveItemListener<Item> listener) {
        support.addActiveItemListener(listener);
    }

    @Override
    public void removeActiveItemListener(ActiveItemListener<Item> listener) {
        support.removeActiveItemListener(listener);
    }

    @Override
    public final Item copyOf() {
        return newInstance()
                .updateFrom((Item) this);
    }

    @Override
    public final boolean maybeUpdateFrom(Item update) {
        if (needsUpdate(update)) {
            Item previous = copyOf();
            Item current = updateFrom(update);
            log.info("Current item has been modified: {}", current);
            support.modified(current, previous);
            return true;
        }
        return false;
    }

    protected abstract Item newInstance();
}
