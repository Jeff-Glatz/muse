package kungzhi.muse.model;

public interface Session {

    void addSessionListener(SessionListener listener);

    void removeSessionListener(SessionListener listener);

    Configuration currentConfiguration();

    void currentConfiguration(Configuration configuration);
}
