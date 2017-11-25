package kungzhi.muse.runtime;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.EegChannel;
import kungzhi.muse.model.Headband;
import kungzhi.muse.osc.service.MessageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Random;
import java.util.SortedSet;
import java.util.concurrent.ExecutorService;

import static java.lang.String.format;

@Service
@Profile("band-power-data")
public class BandPowerDataProducer {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Random random = new Random();
    private final ExecutorService executor;
    private final MessageClient client;
    private final Headband headband;

    private boolean producing;

    public BandPowerDataProducer(ExecutorService executor, MessageClient client, Headband headband) {
        this.executor = executor;
        this.client = client;
        this.headband = headband;
    }

    @PostConstruct
    public void initialize()
            throws Exception {
        log.info("Waiting for configuration before producing data...");
        Configuration configuration = headband.getConfiguration();
        configuration.addActiveItemListener((current, previous) -> {
            if (previous.initial()) {
                log.info("Starting to produce data ...");
                startProducingData(current);
            }
        });
    }

    private void startProducingData(Configuration configuration)
            throws Exception {
        SortedSet<EegChannel> channels = configuration.getEegChannelLayout();
        int arguments = channels.size();
        executor.submit(() -> {
            while (producing) {
                sendBandPower("gamma", false, arguments);
                sendBandPower("beta", false, arguments);
                sendBandPower("alpha", false, arguments);
                sendBandPower("theta", false, arguments);
                sendBandPower("delta", false, arguments);
            }
        });

    }

    private void send(String path, Float[] args)
            throws IOException {
        client.send(new OSCMessage(path, args));
    }

    private void sendBandPower(String band, boolean relative, int argCount) {
        try {
            Float[] arguments = new Float[argCount];
            for (int i = 0; i < argCount; i++) {
                arguments[i] = random.nextFloat();
            }
            send(format("/muse/elements/%s_%s", band, relative ? "relative" : "absolute"),
                    arguments);
        } catch (Exception e) {
            log.error("Failure sending message", e);
        }
    }
}
