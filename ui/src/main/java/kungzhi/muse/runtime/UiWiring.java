package kungzhi.muse.runtime;

import javafx.fxml.FXMLLoader;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan({"kungzhi.muse.ui"})
@Import(OscWiring.class)
@Configuration
@EnableAutoConfiguration
public class UiWiring {

    @Bean
    public FXMLLoader fxmlLoader(ApplicationContext context) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        return loader;
    }
}
