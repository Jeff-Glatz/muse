package kungzhi.muse.model;

import kungzhi.active.ActiveItem;
import kungzhi.active.ActiveItemListener;
import kungzhi.active.ActiveItemSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ActiveModel<M extends ActiveItem<M> & Model>
        extends AbstractModel
        implements ActiveItem<M> {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final transient ActiveItemSupport<M> support =
            new ActiveItemSupport<>();

    public ActiveModel(long time) {
        super(time);
    }

    @Override
    public void addActiveItemListener(ActiveItemListener<M> listener) {
        support.addActiveItemListener(listener);
    }

    @Override
    public void removeActiveItemListener(ActiveItemListener<M> listener) {
        support.removeActiveItemListener(listener);
    }

    @Override
    public final M copy() {
        ActiveModel<M> copy = newInstance();
        return copy.update((M) this);
    }

    @Override
    public final boolean updateFrom(M model) {
        if (!sameAs(model)) {
            M previous = copy();
            M current = update(model);
            log.debug("{} has been modified: {}", getClass().getSimpleName(), current);
            support.modified(current, previous);
            return true;
        }
        return false;
    }

    @Override
    public boolean initial() {
        return time == 0;
    }

    protected abstract M update(M item);


    protected abstract ActiveModel<M> newInstance();
}
