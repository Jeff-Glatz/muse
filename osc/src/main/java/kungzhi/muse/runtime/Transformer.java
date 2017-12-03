package kungzhi.muse.runtime;

import kungzhi.muse.osc.service.MessageAddress;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Component
public @interface Transformer {
    MessageAddress[] value();
}
