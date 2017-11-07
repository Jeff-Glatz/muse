package kungzhi.muse.model;

public class EmptyModelStream<M extends Model>
        implements ModelStream<M> {
    @Override
    public void next(Session session, M model)
            throws Exception {
    }
}
