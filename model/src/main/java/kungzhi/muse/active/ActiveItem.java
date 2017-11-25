package kungzhi.muse.active;

import kungzhi.muse.lang.Equivalence;

import java.io.Serializable;

public interface ActiveItem<Item extends ActiveItem<Item> & Serializable>
        extends Equivalence<Item> {

    void addActiveItemListener(ActiveItemListener<Item> listener);

    void removeActiveItemListener(ActiveItemListener<Item> listener);

    Item copy();

    boolean updateFrom(Item item);

    boolean initial();
}
