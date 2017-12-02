package kungzhi.muse.runtime;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.net.InetAddress;

@ConfigurationProperties("muse.osc")
public class OscProperties
        implements Serializable {
    private String protocol = "tcp";
    private Service receiver = new Service();
    private Service transmitter = new Service();

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String protocol(String defaultProtocol) {
        return protocol != null ? protocol : defaultProtocol;
    }

    public Service getReceiver() {
        return receiver;
    }

    public void setReceiver(Service receiver) {
        this.receiver = receiver;
    }

    public Service getTransmitter() {
        return transmitter;
    }

    public void setTransmitter(Service transmitter) {
        this.transmitter = transmitter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OscProperties osc = (OscProperties) o;

        if (protocol != null ? !protocol.equals(osc.protocol) : osc.protocol != null) return false;
        if (receiver != null ? !receiver.equals(osc.receiver) : osc.receiver != null) return false;
        return transmitter != null ? transmitter.equals(osc.transmitter) : osc.transmitter == null;
    }

    @Override
    public int hashCode() {
        int result = protocol != null ? protocol.hashCode() : 0;
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (transmitter != null ? transmitter.hashCode() : 0);
        return result;
    }

    public static class Service
            implements Serializable {
        private InetAddress host;
        private Integer port = 5000;

        public InetAddress getHost() {
            return host;
        }

        public void setHost(InetAddress host) {
            this.host = host;
        }

        public InetAddress host(InetAddress defaultHost) {
            return host != null ? host : defaultHost;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public Integer port(Integer defaultPort) {
            return port != null ? port : defaultPort;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Service service = (Service) o;

            if (host != null ? !host.equals(service.host) : service.host != null) return false;
            return port != null ? port.equals(service.port) : service.port == null;
        }

        @Override
        public int hashCode() {
            int result = host != null ? host.hashCode() : 0;
            result = 31 * result + (port != null ? port.hashCode() : 0);
            return result;
        }
    }
}
