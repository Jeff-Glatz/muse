package kungzhi.muse.active;

import java.io.Serializable;

public interface ActiveItemListener<Item extends ActiveItem<Item> & Serializable> {
    void modified(Item current, Item previous)
            throws Exception;
}
