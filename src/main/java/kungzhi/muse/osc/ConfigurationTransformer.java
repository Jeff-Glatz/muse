package kungzhi.muse.osc;

import com.jsoniter.any.Any;
import de.sciss.net.OSCMessage;
import kungzhi.muse.model.Configuration;
import kungzhi.muse.model.Sensor;

import java.util.stream.Stream;

import static com.jsoniter.JsonIterator.deserialize;
import static kungzhi.muse.osc.MessageHelper.argumentAt;

public class ConfigurationTransformer
        implements MessageTransformer<Configuration> {

    @Override
    public Configuration fromMessage(OSCMessage message, long time)
            throws Exception {
        Any json = deserialize(argumentAt(message, String.class, 0));
        Configuration configuration = new Configuration();
        Stream.of(json.toString("eeg_channel_layout").trim().split(" "))
                .map(Sensor::valueOf)
                .forEach(configuration::withEegChannelInLayout);
        return configuration;
    }
}
