package kungzhi.muse.model;

public class EmptyModelStream<M extends Model>
        implements ModelStream<M> {
    @Override
    public void next(Headband headband, M model)
            throws Exception {
    }
}
