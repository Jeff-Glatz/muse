package kungzhi.muse.runtime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Clock;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.Runtime.getRuntime;
import static java.net.InetAddress.getByName;
import static java.util.concurrent.Executors.newScheduledThreadPool;

@ComponentScan({
        "kungzhi.muse.model",
        "kungzhi.muse.repository",
        "kungzhi.muse.runtime",
})
@Configuration
public class ModelWiring {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService executorService() {
        return newScheduledThreadPool(getRuntime()
                .availableProcessors());
    }

    @Bean
    public ConversionService conversionService() {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(String.class, InetAddress.class, source -> {
            try {
                return getByName(source);
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException(e);
            }
        });
        return service;
    }
}
