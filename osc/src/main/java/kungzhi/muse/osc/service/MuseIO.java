package kungzhi.muse.osc.service;

import kungzhi.muse.model.Preset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.util.Map;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MuseIO
        implements OscService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private Preset preset = Preset.FOURTEEN;
    private String protocol;
    private InetAddress host;
    private Integer port;
    private long secondsToWaitForShutdown = 30;
    private Process process;

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
        ProcessBuilder builder = processBuilder(System.getProperty("os.name"));
        builder.inheritIO();
        process = builder.start();
        log.info("MuseIO started.");
    }

    @PreDestroy
    public void off() {
        if (process != null) {
            log.info("Stopping MuseIO...");
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

    private ProcessBuilder processBuilder(String platform) {
        if (platform.startsWith("Mac")) {
            ProcessBuilder builder = new ProcessBuilder(
                    "/Applications/Muse/muse-io",
                    "--preset", preset.getId(),
                    "--osc-timestamp",
                    "--osc", format("osc.%s://%s:%d", protocol, host.getHostAddress(), port));
            Map<String, String> environment = builder.environment();
            environment.put("DYLD_LIBRARY_PATH", "/Applications/Muse");
            return builder;

        } else if (platform.startsWith("Windows")) {
            return new ProcessBuilder(
                    "C:\\Program Files (x86)\\Muse\\muse-io",
                    "--preset", preset.getId(),
                    "--osc-timestamp",
                    "--osc", format("osc.%s://%s:%d", protocol, host.getHostAddress(), port));
        }
        throw new UnsupportedPlatformException(
                format("MuseIO on %s is currently not supported by Interaxon", platform));
    }
}
