package kungzhi.muse.runtime;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ResourceBundle;

import static java.util.ResourceBundle.getBundle;

@ComponentScan({"kungzhi.muse.ui"})
@Import(OscWiring.class)
@Configuration
@EnableAutoConfiguration
public class UiWiring {

    @Bean
    public ResourceBundle labels() {
        return getBundle("kungzhi.muse.ui.Labels");
    }
}
