package kungzhi.muse.runtime;

import kungzhi.muse.platform.MuseHeadband;
import kungzhi.muse.platform.MuseHeadbands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.bluetooth.BluetoothStateException;

import static java.lang.Thread.sleep;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)
            throws BluetoothStateException, InterruptedException {
        Thread thread = new Thread(() -> {
            ConfigurableApplicationContext context =
                    new SpringApplicationBuilder(MuseConfiguration.class)
                            .headless(false)
                            .web(false)
                            .registerShutdownHook(true)
                            .properties()
                            .run();
            MuseHeadbands headbands = context.getBean(MuseHeadbands.class);
            MuseHeadband headband = headbands.lookup("000666794833");
            headband.connect();
            log.info("RSSI: {}", headband.getRssi());
            try {
                sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(false);
        thread.start();
    }
}
