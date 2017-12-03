package kungzhi.muse.osc.simulation;

import com.illposed.osc.OSCMessage;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Headband;
import kungzhi.muse.osc.service.MessageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.String.format;
import static java.lang.Thread.sleep;

@Component
@Profile("simulation")
@ManagedResource
public class BandPowerSimulator {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Random random = new Random();
    private final ScheduledExecutorService executor;
    private final MessageClient client;
    private final Headband headband;

    private boolean relative = true;
    private boolean producing;
    private long latencyFactor = 300;

    public BandPowerSimulator(ScheduledExecutorService executor, MessageClient client, Headband headband) {
        this.executor = executor;
        this.client = client;
        this.headband = headband;
    }

    @ManagedAttribute
    public boolean isProducing() {
        return producing;
    }

    @ManagedAttribute
    public boolean isRelative() {
        return relative;
    }

    @ManagedAttribute
    public void setRelative(boolean relative) {
        this.relative = relative;
    }

    @ManagedAttribute
    public long getLatencyFactor() {
        return latencyFactor;
    }

    @ManagedAttribute
    public void setLatencyFactor(long latencyFactor) {
        this.latencyFactor = latencyFactor;
    }

    @ManagedOperation
    public void start()
            throws Exception {
        Configuration configuration = headband.getConfiguration();
        int sensors = configuration.getEegChannelCount();
        log.info("Producing random {} band power data for {} sensors",
                relative ? "relative" : "absolute", sensors);
        executor.submit(() -> {
            producing = true;
            try {
                while (producing) {
                    executor.execute(() -> {
                        sendBandPower("gamma", sensors);
                        sendBandPower("beta", sensors);
                        sendBandPower("alpha", sensors);
                        sendBandPower("theta", sensors);
                        sendBandPower("delta", sensors);
                    });
                    sleep(computeRandomLatency());
                }
            } finally {
                producing = false;
            }
            return null;
        });
    }

    @ManagedOperation
    @PreDestroy
    public void stop() {
        producing = false;
    }

    private void send(String address, Collection args)
            throws IOException {
        client.send(new OSCMessage(address, args));
    }

    private void sendBandPower(String band, int sensors) {
        try {
            List<Float> arguments = new ArrayList<>(sensors);
            for (int i = 0; i < sensors; i++) {
                arguments.add(random.nextFloat());
            }
            send(format("/muse/elements/%s_%s", band, relative ? "relative" : "absolute"),
                    arguments);
        } catch (Exception e) {
            log.error("Failure sending message", e);
        }
    }

    private long computeRandomLatency() {
        return (long) (latencyFactor * random.nextDouble());
    }
}
