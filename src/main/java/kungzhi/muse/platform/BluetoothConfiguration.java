package kungzhi.muse.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;

@Configuration
@Profile("bluetooth")
@ComponentScan("kungzhi.muse.platform")
public class BluetoothConfiguration {
    private final Logger log = LoggerFactory.getLogger(BluetoothConfiguration.class);

    @Bean
    LocalDevice localDevice()
            throws BluetoothStateException {
        LocalDevice device = LocalDevice.getLocalDevice();
        log.info("Retrieved local bluetooth device named {}", device.getFriendlyName());
        return device;
    }
}
