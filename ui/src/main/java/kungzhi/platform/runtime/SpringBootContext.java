package kungzhi.platform.runtime;

import javafx.application.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;

import static org.springframework.boot.Banner.Mode.CONSOLE;

public class SpringBootContext
        implements ControllerFactory {
    private final Application application;
    private ConfigurableApplicationContext context;

    public SpringBootContext(Application application) {
        this.application = application;
    }

    public void run()
            throws Exception {
        context = new SpringApplicationBuilder(application.getClass())
                .environment(new StandardEnvironment())
                .registerShutdownHook(true)
                .bannerMode(CONSOLE)
                .headless(false)
                .web(false)
                .properties(new HashMap<>(application.getParameters().getNamed()))
                .initializers(context -> context.getBeanFactory()
                        .registerSingleton("application", application))
                .initializers(context -> context.getBeanFactory()
                        .registerSingleton("controllerFactory",
                                SpringBootContext.this))
                .run(application.getParameters().getRaw().toArray(new String[0]));
        context.getAutowireCapableBeanFactory()
                .autowireBean(application);
        context.getAutowireCapableBeanFactory()
                .autowireBean(this);
    }

    public <T> T load(Class<T> type) {
        return context.getBean(type);
    }

    public <T> T load(String name, Class<T> type) {
        return context.getBean(name, type);
    }

    public void stop() {
        context.close();
    }
}
