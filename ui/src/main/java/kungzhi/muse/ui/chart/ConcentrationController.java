package kungzhi.muse.ui.chart;

import kungzhi.muse.model.Headband;
import kungzhi.muse.osc.service.MessageDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.Clock;

import static kungzhi.muse.osc.service.MessagePath.CONCENTRATION;

@Controller
public class ConcentrationController
        extends SingleNumberController {

    @Autowired
    public ConcentrationController(Clock clock, Headband headband,
                                   MessageDispatcher dispatcher) {
        super(clock, headband, dispatcher, CONCENTRATION, "model.algorithm.concentration");
    }
}
