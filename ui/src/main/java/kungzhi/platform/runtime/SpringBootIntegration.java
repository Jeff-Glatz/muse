package kungzhi.platform.runtime;

import javafx.fxml.FXMLLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SpringBootIntegration {

    @Bean
    @Scope("prototype")
    public FXMLLoader loader(ControllerFactory factory) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(factory::load);
        return loader;
    }
}
