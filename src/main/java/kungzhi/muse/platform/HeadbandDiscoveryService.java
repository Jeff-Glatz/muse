package kungzhi.muse.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.format;

@Service
public class HeadbandDiscoveryService
        implements MuseHeadbands {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Map<String, MuseHeadband> headbands = new HashMap<>();
    private final DiscoveryListener listener = new DiscoveryListener() {
        @Override
        public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
            discovered(remoteDevice);
        }

        @Override
        public void servicesDiscovered(int transactionId, ServiceRecord[] records) {
            Stream.of(records).forEach(record -> {
                RemoteDevice device = record.getHostDevice();
                log.info("Service was discovered: {}/{}", record, device);
            });
        }

        @Override
        public void serviceSearchCompleted(int transactionId, int responseCode) {
            log.info("Service search completed. Transaction ID: {}, Response Code: {}",
                    transactionId, responseCode);
        }

        @Override
        public void inquiryCompleted(int status) {
            log.info("Inquiry completed. Status: {}", status);
        }
    };
    private final LocalDevice localDevice;

    @Autowired
    public HeadbandDiscoveryService(LocalDevice localDevice) {
        this.localDevice = localDevice;
    }

    @PostConstruct
    public void initialize()
            throws Exception {
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();
        RemoteDevice[] remoteDevices = agent
                .retrieveDevices(DiscoveryAgent.PREKNOWN);
        if (remoteDevices != null) {
            Stream.of(remoteDevices).forEach(this::discovered);
        }
    }

    @Override
    public Collection<MuseHeadband> all() {
        return headbands.values();
    }

    @Override
    public MuseHeadband lookup(String macAddress) {
        MuseHeadband headband = headbands.get(macAddress);
        if (headband == null) {
            throw new IllegalArgumentException(
                    format("No headband exists with address: %s", macAddress));
        }
        return headband;
    }

    public void startInquiry()
            throws BluetoothStateException {
        log.info("Starting bluetooth device inquiry...");
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();
        agent.startInquiry(DiscoveryAgent.GIAC, listener);
        log.info("Bluetooth device inquiry started.");
    }

    @PreDestroy
    public void cancelInquiry()
            throws BluetoothStateException {
        log.info("Canceling bluetooth device inquiry...");
        localDevice.getDiscoveryAgent()
                .cancelInquiry(listener);
        log.info("Bluetooth device inquiry canceled.");
    }

    private boolean isHeadband(RemoteDevice device) {
        try {
            String name = device.getFriendlyName(false);
            return name.toLowerCase().startsWith("muse-");
        } catch (Exception e) {
            log.error("Failure examining bluetooth device", e);
            return false;
        }
    }

    private void discovered(RemoteDevice remoteDevice) {
        log.info("Remote device was discovered: {}", localDevice);
        if (isHeadband(remoteDevice)) {
            String address = remoteDevice.getBluetoothAddress();
            log.info("Discovered muse headband: {}", address);
            headbands.put(address, new MuseHeadband(remoteDevice));
        }
    }
}
