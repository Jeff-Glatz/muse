package kungzhi.muse.platform;

import com.choosemuse.libmuse.ConnectionState;
import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseConfiguration;
import com.choosemuse.libmuse.MuseConnectionListener;
import com.choosemuse.libmuse.MuseDataListener;
import com.choosemuse.libmuse.MuseDataPacketType;
import com.choosemuse.libmuse.MuseErrorListener;
import com.choosemuse.libmuse.MusePreset;
import com.choosemuse.libmuse.MuseVersion;
import com.choosemuse.libmuse.NotchFrequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.util.StreamUtils;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

import static com.intel.bluetooth.RemoteDeviceHelper.readRSSI;
import static java.lang.String.format;
import static javax.microedition.io.Connector.open;

public class MuseHeadband
        extends Muse {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final RemoteDevice device;
    private final DirectFieldAccessor deviceAccessor;
    private ConnectionState connectionState = ConnectionState.UNKNOWN;
    private StreamConnection connection;

    public MuseHeadband(RemoteDevice device) {
        this.device = device;
        this.deviceAccessor = new DirectFieldAccessor(device);
    }

    @Override
    public ConnectionState getConnectionState() {
        return connectionState;
    }

    @Override
    public String getMacAddress() {
        return device.getBluetoothAddress();
    }

    @Override
    public String getName() {
        return apply(() -> device.getFriendlyName(false), "Failure getting name from remote device");
    }

    @Override
    public boolean isPaired() {
        return (boolean) deviceAccessor.getPropertyValue("paired");
    }

    @Override
    public final void connect() {
        connectionState = ConnectionState.CONNECTING;
        try {
            connection = apply(() -> (StreamConnection) open(format("btspp://%s:2", device.getBluetoothAddress())),
                    "Failure opening connection to remote device");
            connectionState = ConnectionState.CONNECTED;
        } catch (RuntimeException e) {
            connectionState = ConnectionState.DISCONNECTED;
            throw e;
        }
    }

    public void readFromInputStream() {
        attempt(() -> {
            log.info("Attempting to read from stream");
            StreamUtils.copy(connection.openDataInputStream(), System.out);
        }, "Failure reading from input stream");
    }

    @Override
    public final void disconnect() {
        try {
            attempt(() -> connection.close(), "Failure closing connection");
        } finally {
            connection = null;
            connectionState = ConnectionState.DISCONNECTED;
        }
    }

    @Override
    public void execute() {

    }

    @Override
    public void runAsynchronously() {

    }

    @Override
    public double getRssi() {
        return apply(() -> (double) readRSSI(device), "Failure reading RSSI");
    }

    @Override
    public double getLastDiscoveredTime() {
        return 0;
    }

    @Override
    public void setNumConnectTries(int i) {
    }

    @Override
    public MuseConfiguration getMuseConfiguration() {
        return null;
    }

    @Override
    public MuseVersion getMuseVersion() {
        return null;
    }

    @Override
    public void registerConnectionListener(MuseConnectionListener museConnectionListener) {

    }

    @Override
    public void unregisterConnectionListener(MuseConnectionListener museConnectionListener) {

    }

    @Override
    public void registerDataListener(MuseDataListener museDataListener, MuseDataPacketType museDataPacketType) {

    }

    @Override
    public void unregisterDataListener(MuseDataListener museDataListener, MuseDataPacketType museDataPacketType) {

    }

    @Override
    public void registerErrorListener(MuseErrorListener museErrorListener) {

    }

    @Override
    public void unregisterErrorListener(MuseErrorListener museErrorListener) {

    }

    @Override
    public void unregisterAllListeners() {

    }

    @Override
    public void setPreset(MusePreset musePreset) {

    }

    @Override
    public void enableDataTransmission(boolean b) {

    }

    @Override
    public void setNotchFrequency(NotchFrequency notchFrequency) {

    }

    @Override
    public boolean isLowEnergy() {
        return false;
    }

    @Override
    public void enableException(boolean b) {
    }

    private <R> R apply(ExceptionalFunction<R> function, String failureStatement) {
        try {
            return function.apply();
        } catch (Exception e) {
            log.error(failureStatement, e);
            throw new MuseHeadbandException(this, e);
        }
    }

    private void attempt(ExceptionalAction action, String failureStatement) {
        try {
            action.attempt();
        } catch (Exception e) {
            log.error(failureStatement, e);
            throw new MuseHeadbandException(this, e);
        }
    }

    @FunctionalInterface
    interface ExceptionalFunction<R> {
        R apply() throws Exception;
    }

    @FunctionalInterface
    interface ExceptionalAction {
        void attempt() throws Exception;
    }
}
