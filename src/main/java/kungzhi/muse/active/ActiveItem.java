package kungzhi.muse.active;

import java.io.Serializable;

public interface ActiveItem<Item extends ActiveItem<Item> & Serializable> {

    void addActiveItemListener(ActiveItemListener<Item> listener);

    void removeActiveItemListener(ActiveItemListener<Item> listener);

    boolean needsUpdate(Item item);

    Item copyOf();

    Item updateFrom(Item item);

    boolean maybeUpdateFrom(Item item);
}
