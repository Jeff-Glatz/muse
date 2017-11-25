package kungzhi.muse.runtime;

import javafx.application.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;

import static org.springframework.boot.Banner.Mode.CONSOLE;

public class SpringContext {
    private final Application application;
    private ConfigurableApplicationContext context;

    public SpringContext(Application application) {
        this.application = application;
    }

    public void init()
            throws Exception {
        context = new SpringApplicationBuilder(UiWiring.class)
                .environment(new StandardEnvironment())
                .registerShutdownHook(true)
                .bannerMode(CONSOLE)
                .headless(false)
                .web(false)
                .properties(new HashMap<>(application.getParameters().getNamed()))
                .initializers(context -> context.getBeanFactory()
                        .registerSingleton("application", application))
                .run(application.getParameters().getRaw().toArray(new String[0]));
        context.getAutowireCapableBeanFactory()
                .autowireBean(application);
    }

    public <T> T lookup(Class<T> type) {
        return context.getBean(type);
    }

    public <T> T lookup(String name, Class<T> type) {
        return context.getBean(name, type);
    }

    public void close() {
        context.close();
    }
}
