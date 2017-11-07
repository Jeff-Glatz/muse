package kungzhi.muse.ui;

import javafx.concurrent.Task;
import kungzhi.muse.model.Model;
import kungzhi.muse.model.ModelStream;
import kungzhi.muse.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

public class AsyncModelStream<M extends Model>
        implements ModelStream<M> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Executor executor;
    private final ModelStream<M> delegate;

    public AsyncModelStream(Executor executor, ModelStream<M> delegate) {
        this.executor = executor;
        this.delegate = delegate;
    }

    @Override
    public void next(final Session session, final M model)
            throws Exception {
        executor.execute(new Task<M>() {
            @Override
            protected M call()
                    throws Exception {
                return model;
            }

            @Override
            protected void succeeded() {
                // Stream the event here so that updates can occur on GUI thread
                try {
                    delegate.next(session, model);
                } catch (Exception e) {
                    log.error("Failure streaming model", e);
                }
            }
        });
    }

    public static <M extends Model> ModelStream<M> inBackground(Executor executor, ModelStream<M> delegate) {
        return new AsyncModelStream<>(executor, delegate);
    }
}
