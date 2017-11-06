package kungzhi.muse.stream;

import kungzhi.muse.model.Session;

import java.io.Serializable;

public interface ModelStream<Model extends Serializable> {
    void next(Session session, Model model);
}
