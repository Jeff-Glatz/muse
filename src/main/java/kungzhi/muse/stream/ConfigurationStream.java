package kungzhi.muse.stream;

import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Session;

public class ConfigurationStream
        implements ModelStream<Configuration> {

    @Override
    public void next(Session session, Configuration configuration) {
        session.setConfiguration(configuration);
    }
}
