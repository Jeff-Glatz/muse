package kungzhi.muse.active;

import java.io.Serializable;

public interface ActiveItem<Item extends ActiveItem<Item> & Serializable> {

    void addActiveItemListener(ActiveItemListener<Item> listener);

    void removeActiveItemListener(ActiveItemListener<Item> listener);

    boolean differsFrom(Item item);

    Item copy();

    boolean updateFrom(Item item);

    boolean initial();
}
