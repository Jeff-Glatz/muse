package kungzhi.muse.runtime;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

@Import(PlatformWiring.class)
@EnableAutoConfiguration
@SpringBootApplication
public class PlatformHarness {
    public static void main(String[] args)
            throws Exception {
        new SpringApplicationBuilder(PlatformHarness.class)
                .headless(false)
                .web(false)
                .registerShutdownHook(true)
                .properties()
                .run(args);
    }
}
