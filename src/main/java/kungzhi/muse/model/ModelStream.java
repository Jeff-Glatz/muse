package kungzhi.muse.model;

import java.io.Serializable;

public interface ModelStream<Model extends Serializable> {
    void next(Session session, Model model)
            throws Exception;
}
