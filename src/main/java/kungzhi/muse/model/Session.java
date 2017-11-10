package kungzhi.muse.model;

public interface Session {

    Configuration getConfiguration();

    Version getVersion();

    Battery getBattery();

    DrlReference getDrlReference();
}
