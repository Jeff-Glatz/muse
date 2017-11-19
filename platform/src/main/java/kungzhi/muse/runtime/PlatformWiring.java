package kungzhi.muse.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;

@ComponentScan({"kungzhi.muse.platform"})
@Import(ModelWiring.class)
@Configuration
public class PlatformWiring {
    private final Logger log = LoggerFactory.getLogger(PlatformWiring.class);

    @Bean
    public LocalDevice localDevice()
            throws BluetoothStateException {
        LocalDevice device = LocalDevice.getLocalDevice();
        log.info("Retrieved local bluetooth device named {}", device.getFriendlyName());
        return device;
    }
}
