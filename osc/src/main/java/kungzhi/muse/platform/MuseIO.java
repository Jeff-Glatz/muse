package kungzhi.muse.platform;

import kungzhi.lang.ServiceControl;
import kungzhi.muse.model.Preset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MuseIO
        implements ServiceControl {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Logger museIOLog = LoggerFactory.getLogger("MuseIO");
    private final List<ConnectionListener> listeners = new ArrayList<>();

    private Thread monitoringThread;
    private Preset preset = Preset.FOURTEEN;
    private String protocol;
    private InetSocketAddress socketAddress;
    private long secondsToWaitForShutdown = 30;
    private Process process;

    Boolean connected;

    public void addConnectionListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    public void removeConnectionListener(ConnectionListener listener) {
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

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(InetSocketAddress host) {
        this.socketAddress = host;
    }

    public MuseIO withSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
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
                    "--no-ansi",
                    "--preset", preset.getId().toLowerCase(),
                    "--osc", osc());
            Map<String, String> environment = builder.environment();
            environment.put("DYLD_LIBRARY_PATH", "/Applications/Muse");
            return builder;

        } else if (platform.startsWith("Windows")) {
            return new ProcessBuilder(
                    "C:\\Program Files (x86)\\Muse\\muse-io",
                    "--no-ansi",
                    "--preset", preset.getId().toLowerCase(),
                    "--osc", osc());
        }
        throw new UnsupportedPlatformException(
                format("MuseIO on %s is currently not supported by Interaxon", platform));
    }

    void fireConnected(boolean connected) {
        if (this.connected == null || (this.connected ^ connected)) {
            this.connected = connected;
            listeners.forEach(listener -> {
                try {
                    listener.onConnected(connected);
                } catch (Exception e) {
                    log.error("Failure notifying listener", e);
                }
            });
        }
    }

    private String osc() {
        return format("osc.%s://%s:%d",
                protocol,
                socketAddress.getAddress().getHostAddress(),
                socketAddress.getPort());
    }

    private void startMonitoringOutput() {
        log.info("Starting MuseIO monitoring thread...");
        monitoringThread = new Thread(() -> {
            Scanner sc = new Scanner(process.getInputStream());
            fireConnected(false);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                museIOLog.info(line);
                if (line.contains("Connected.")) {
                    fireConnected(true);
                } else if (line.contains("Connection failure")) {
                    fireConnected(false);
                } else if (line.contains("OSC error 61")) {
                    // TODO: No client endpoint connected
                }
            }
        }, "MuseIO Monitor");
        monitoringThread.setDaemon(true);
        monitoringThread.start();
        log.info("MuseIO monitoring thread started.");
    }

    private void stopMonitoringOutput() {
        try {
            if (monitoringThread != null) {
                log.info("Stopping MuseIO monitoring thread...");
                monitoringThread.interrupt();
                log.info("MuseIO monitoring thread stopped.");
            }
        } finally {
            monitoringThread = null;
            connected = null;
        }
    }
}
