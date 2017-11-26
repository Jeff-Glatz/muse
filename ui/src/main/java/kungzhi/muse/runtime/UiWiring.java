package kungzhi.muse.runtime;

import javafx.fxml.FXMLLoader;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

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

    @Bean
    @Scope("prototype")
    public FXMLLoader loader(ApplicationContext context, ResourceBundle labels) {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(labels);
        loader.setControllerFactory(context::getBean);
        return loader;
    }
}
