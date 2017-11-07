package kungzhi.muse.ui;

import javafx.application.Platform;
import kungzhi.muse.model.Model;
import kungzhi.muse.model.ModelStream;
import kungzhi.muse.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javafx.application.Platform.runLater;

public class AsyncModelStream<M extends Model>
        implements ModelStream<M> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ModelStream<M> delegate;

    private AsyncModelStream(ModelStream<M> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void next(final Session session, final M model)
            throws Exception {
        runLater(() -> {
            try {
                delegate.next(session, model);
            } catch (Exception e) {
                log.error("Failure streaming model", e);
            }
        });
    }

    public static <M extends Model> ModelStream<M> onFxApplicationThread(ModelStream<M> delegate) {
        return new AsyncModelStream<>(delegate);
    }
}
