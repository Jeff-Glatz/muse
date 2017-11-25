package kungzhi.muse.osc.service;

import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.EegChannel;
import kungzhi.muse.model.Headband;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Random;
import java.util.SortedSet;
import java.util.concurrent.ExecutorService;

import static java.lang.String.format;

@Component
@Profile("mock-data-generation")
@ManagedResource
public class BandPowerDataProducer {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Random random = new Random();
    private final ExecutorService executor;
    private final MessageClient client;
    private final Headband headband;

    private boolean producing;
    private long latency = 100;

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

    @ManagedAttribute
    public boolean isProducing() {
        return producing;
    }

    @ManagedAttribute
    public long getLatency() {
        return latency;
    }

    @ManagedAttribute
    public void setLatency(long latency) {
        this.latency = latency;
    }

    @ManagedOperation
    public void start() {
        startProducingData(headband.getConfiguration());
    }

    @ManagedOperation
    @PreDestroy
    public void stop() {
        producing = false;
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

    private void addLatency() {
        try {
            Thread.sleep(latency);
        } catch (InterruptedException e) {
            log.error("Thread was interrupted", e);
        }
    }

    private void startProducingData(Configuration configuration) {
        SortedSet<EegChannel> channels = configuration.getEegChannelLayout();
        int arguments = channels.size();
        producing = true;
        executor.submit(() -> {
            while (producing) {
                sendBandPower("gamma", false, arguments);
                sendBandPower("beta", false, arguments);
                sendBandPower("alpha", false, arguments);
                sendBandPower("theta", false, arguments);
                sendBandPower("delta", false, arguments);
                addLatency();
            }
        });
    }
}
