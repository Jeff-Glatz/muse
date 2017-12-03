package kungzhi.muse.ui.chart;

import kungzhi.muse.model.Headband;
import kungzhi.muse.osc.service.MessageDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.Clock;

import static kungzhi.muse.osc.service.MessagePath.MELLOW;

@Controller
public class MellowController
        extends SingleNumberController {

    @Autowired
    public MellowController(Clock clock, Headband headband,
                            MessageDispatcher dispatcher) {
        super(clock, headband, dispatcher, MELLOW, "model.algorithm.mellow");
    }
}
