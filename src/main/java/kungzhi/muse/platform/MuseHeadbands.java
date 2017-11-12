package kungzhi.muse.platform;

import java.util.Collection;

public interface MuseHeadbands {
    Collection<MuseHeadband> all();
    MuseHeadband lookup(String macAddress);
}
