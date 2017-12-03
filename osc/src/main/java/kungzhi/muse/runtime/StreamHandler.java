package kungzhi.muse.runtime;

import kungzhi.muse.osc.service.MessageAddress;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface StreamHandler {
    MessageAddress value();
}
