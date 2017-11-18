package kungzhi.muse.osc.service;

import kungzhi.muse.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import static java.util.Arrays.stream;

@Component
public class StreamPostProcessor
        implements BeanPostProcessor {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MessageDispatcher dispatcher;

    @Autowired
    public StreamPostProcessor(MessageDispatcher dispatcher) {
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
        Class<?> streamType = bean.getClass();
        if (streamType.isAnnotationPresent(StreamComponent.class)) {
            stream(streamType.getMethods()).forEach(method -> {
                StreamHandler handler = method.getDeclaredAnnotation(StreamHandler.class);
                if (handler != null) {
                    Class<? extends Model> modelType = (Class<? extends Model>) method.getParameterTypes()[1];
                    dispatcher.withStream(handler.value(), modelType, (headband, model) ->
                            method.invoke(bean, headband, model));
                    log.info("Streaming {} models on {} to {}",
                            modelType.getSimpleName(),
                            handler.value().getName(),
                            method.toGenericString());
                }
            });
        }
        return bean;
    }
}
