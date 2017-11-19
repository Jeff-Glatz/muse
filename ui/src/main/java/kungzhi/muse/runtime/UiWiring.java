package kungzhi.muse.runtime;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan({"kungzhi.muse.ui"})
@Import(OscWiring.class)
@Configuration
public class UiWiring {
}
