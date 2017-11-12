package kungzhi.muse.model;

import java.io.Serializable;

public interface ModelStream<Model extends Serializable> {
    void next(Headband headband, Model model)
            throws Exception;
}
