package kungzhi.muse.osc.service;

import kungzhi.muse.model.ModelStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

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
        Stream stream = bean.getClass().getAnnotation(Stream.class);
        if (stream != null) {
            if (!(bean instanceof ModelStream)) {
                throw new TypeMismatchException(bean, ModelStream.class);
            }
            dispatcher.withStream(stream.path(), stream.type(), (ModelStream) bean);
            log.info("Streaming {} models of type {} to {}",
                    stream.path().getName(),
                    stream.type().getSimpleName(),
                    bean.getClass().getSimpleName());
        }
        return bean;
    }
}
