package kungzhi.muse.osc.service;

import kungzhi.muse.lang.ServiceControl;
import kungzhi.muse.model.Preset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MuseIO
        implements ServiceControl {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Logger muse = LoggerFactory.getLogger("MuseIO");
    private final List<HeadbandPairingListener> listeners = new ArrayList<>();

    private Thread monitoringThread;
    private Preset preset = Preset.FOURTEEN;
    private String protocol;
    private InetAddress host;
    private Integer port;
    private long secondsToWaitForShutdown = 30;
    private Process process;

    public void addHeadbandPairingListener(HeadbandPairingListener listener) {
        listeners.add(listener);
    }

    public void removeHeadbandPairingListener(HeadbandPairingListener listener) {
        listeners.remove(listener);
    }

    public Preset getPreset() {
        return preset;
    }

    public void setPreset(Preset preset) {
        this.preset = preset;
    }

    public MuseIO withPreset(Preset preset) {
        this.preset = preset;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public MuseIO withProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public InetAddress getHost() {
        return host;
    }

    public void setHost(InetAddress host) {
        this.host = host;
    }

    public MuseIO withHost(InetAddress host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public MuseIO withPort(Integer port) {
        this.port = port;
        return this;
    }

    public long getSecondsToWaitForShutdown() {
        return secondsToWaitForShutdown;
    }

    public void setSecondsToWaitForShutdown(long secondsToWaitForShutdown) {
        this.secondsToWaitForShutdown = secondsToWaitForShutdown;
    }

    public MuseIO withSecondsToWaitForShutdown(long secondsToWaitForShutdown) {
        this.secondsToWaitForShutdown = secondsToWaitForShutdown;
        return this;
    }

    public void on()
            throws Exception {
        log.info("Starting MuseIO ...");
        process = processBuilder(System.getProperty("os.name"))
                .redirectErrorStream(true)
                .start();
        startMonitoringOutput();
        log.info("MuseIO started.");
    }

    @PreDestroy
    public void off() {
        if (process != null) {
            log.info("Stopping MuseIO...");
            try {
                stopMonitoringOutput();
            } finally {
                try {
                    process.destroyForcibly()
                            .waitFor(secondsToWaitForShutdown, SECONDS);
                    log.info("MuseIO stopped.");
                } catch (Exception e) {
                    log.error("Failure stopping MuseIO", e);
                } finally {
                    process = null;
                }
            }
        }
    }

    private ProcessBuilder processBuilder(String platform) {
        if (platform.startsWith("Mac")) {
            ProcessBuilder builder = new ProcessBuilder(
                    "/Applications/Muse/muse-io",
                    "--preset", preset.getId().toLowerCase(),
                    "--osc-timestamp",
                    "--osc", format("osc.%s://%s:%d", protocol, host.getHostAddress(), port));
            Map<String, String> environment = builder.environment();
            environment.put("DYLD_LIBRARY_PATH", "/Applications/Muse");
            return builder;

        } else if (platform.startsWith("Windows")) {
            return new ProcessBuilder(
                    "C:\\Program Files (x86)\\Muse\\muse-io",
                    "--preset", preset.getId().toLowerCase(),
                    "--osc-timestamp",
                    "--osc", format("osc.%s://%s:%d", protocol, host.getHostAddress(), port));
        }
        throw new UnsupportedPlatformException(
                format("MuseIO on %s is currently not supported by Interaxon", platform));
    }

    private void firePaired(boolean paired) {
        listeners.forEach(listener -> {
            try {
                listener.onHeadbandPaired(paired);
            } catch (Exception e) {
                log.error("Failure notifying listener", e);
            }
        });
    }

    private void startMonitoringOutput() {
        log.info("Starting MuseIO output monitoring thread...");
        monitoringThread = new Thread(() -> {
            Scanner sc = new Scanner(process.getInputStream());
            firePaired(false);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                muse.info(line);
                if (line.contains("receiving at:")) {
                    firePaired(true);
                } else if (line.contains("Connection failure")) {
                    firePaired(false);
                } else if (line.contains("OSC error 61")) {
                    // TODO: No client endpoint connected
                }
            }
        });
        monitoringThread.setDaemon(true);
        monitoringThread.start();
        log.info("MuseIO output monitoring thread started.");
    }

    private void stopMonitoringOutput() {
        try {
            if (monitoringThread != null) {
                log.info("Stopping MuseIO output monitoring thread...");
                monitoringThread.interrupt();
                log.info("MuseIO output monitoring thread stopped.");
            }
        } finally {
            monitoringThread = null;
        }
    }
}
