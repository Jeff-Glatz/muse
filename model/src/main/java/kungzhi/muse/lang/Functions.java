package kungzhi.muse.lang;

import java.util.stream.Stream;

import static java.lang.Float.isNaN;

public class Functions {

    public static Double averageOf(Stream<Float> values) {
        return values.filter(value -> value != null && !isNaN(value))
                .mapToDouble(value -> value)
                .average()
                .getAsDouble();
    }
}
