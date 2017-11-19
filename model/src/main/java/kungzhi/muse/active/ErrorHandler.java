package kungzhi.muse.active;

import java.io.Serializable;

public interface ErrorHandler<Item extends ActiveItem<Item> & Serializable> {
    void error(ActiveItemListener<Item> listener, Exception ex);
}
