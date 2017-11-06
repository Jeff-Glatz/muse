package kungzhi.muse.controller;

import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Session;

import javax.annotation.Resource;

@Resource
public class ConfigurationController
        implements Controller<Configuration> {

    @Override
    public void update(Session session, Configuration configuration) {
        session.currentConfiguration(configuration);
    }
}
