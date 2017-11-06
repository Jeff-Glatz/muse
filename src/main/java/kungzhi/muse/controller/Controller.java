package kungzhi.muse.controller;

import kungzhi.muse.model.Session;

import java.io.Serializable;

public interface Controller<Model extends Serializable> {
    void update(Session session, Model model);
}
