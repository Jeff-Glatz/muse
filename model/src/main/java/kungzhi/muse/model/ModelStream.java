package kungzhi.muse.model;

import java.io.Serializable;

@FunctionalInterface
public interface ModelStream<Model extends Serializable> {
    void next(Headband headband, Model model)
            throws Exception;
}
