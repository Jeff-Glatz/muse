package kungzhi.muse.ui;

import kungzhi.muse.model.Headband;
import kungzhi.muse.model.HeadbandStatus;
import kungzhi.muse.model.ModelStream;
import kungzhi.muse.osc.service.Stream;
import org.springframework.stereotype.Component;

import static kungzhi.muse.osc.service.MessagePath.HEADBAND_STATUS;

@Component
@Stream(path = HEADBAND_STATUS, type = HeadbandStatus.class)
public class HeadbandStatusUi
        implements ModelStream<HeadbandStatus> {

    @Override
    public void next(Headband headband, HeadbandStatus status)
            throws Exception {

    }
}
