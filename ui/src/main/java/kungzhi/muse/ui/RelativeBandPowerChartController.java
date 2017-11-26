package kungzhi.muse.ui;

import kungzhi.muse.model.Headband;
import kungzhi.muse.osc.service.MessageDispatcher;
import kungzhi.muse.repository.Bands;
import org.springframework.beans.factory.annotation.Autowired;

public class RelativeBandPowerChartController
        extends BandPowerChartController {

    @Autowired
    public RelativeBandPowerChartController(
            Bands bands,
            Headband headband,
            MessageDispatcher dispatcher) {
        super(bands, headband, dispatcher, true);
    }
}
