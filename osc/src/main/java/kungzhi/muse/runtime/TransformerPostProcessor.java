package kungzhi.muse.runtime;

import kungzhi.muse.osc.service.MessageDispatcher;
import kungzhi.muse.osc.service.Transformer;
import kungzhi.muse.osc.transform.MessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import static java.util.Arrays.stream;

/**
 * A {@link BeanPostProcessor} that scans for {@link Transformer} annotated
 * {@link MessageTransformer} instances and auto-magically attaches the
 * {@link MessageTransformer} to the {@link MessageDispatcher}.
 *
 * @see MessageTransformer
 * @see Transformer
 */
@Component
public class TransformerPostProcessor implements BeanPostProcessor {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MessageDispatcher dispatcher;

    @Autowired
    public TransformerPostProcessor(MessageDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof MessageTransformer) {
            Class<?> implementation = bean.getClass();
            if (implementation.isAnnotationPresent(Transformer.class)) {
                Transformer transformer = implementation.getAnnotation(Transformer.class);
                stream(transformer.value()).forEach(path -> {
                    dispatcher.withTransformer(path, path.getType(), (MessageTransformer<?>) bean);
                    log.info("Transforming messages on {} into {} using {}",
                            path.getName(),
                            path.getType().getSimpleName(),
                            implementation.getSimpleName());
                });
            }
        }
        return bean;
    }
}