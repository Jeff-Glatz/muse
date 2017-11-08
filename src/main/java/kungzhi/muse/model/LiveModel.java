package kungzhi.muse.model;

public interface LiveModel<M extends LiveModel<M> & Model> {
    M copyOf();
    boolean needsUpdate(M model);
    M updateFrom(M model);
}
