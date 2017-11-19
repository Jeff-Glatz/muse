package kungzhi.muse.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

public class ProblemLog {
    private static final Logger log = LoggerFactory.getLogger("kungzhi.muse.runtime.Problem");

    public static void problem(Class caller, String message, Object... args) {
        log.warn(format("%s: %s",
                caller.getSimpleName(),
                format(message, args)));
    }
}
