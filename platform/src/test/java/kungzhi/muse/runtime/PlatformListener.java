package kungzhi.muse.runtime;

import kungzhi.muse.platform.MuseHeadband;
import kungzhi.muse.platform.MuseHeadbands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class PlatformListener
        implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Platform application started, looking for headband by MAC address");
        ApplicationContext context = event.getApplicationContext();
        MuseHeadbands headbands = context.getBean(MuseHeadbands.class);
        MuseHeadband headband = headbands.lookup("000666794833");
        log.info("Found headband at: {}", headband.getMacAddress());
        headband.connect();
        headband.readFromInputStream();
    }
}
