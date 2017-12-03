package kungzhi.muse.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Values<T>
        implements Serializable {
    private final Class<T> type;
    private final List<Object> values;

    public Values(Class<T> type, List<Object> values) {
        this.type = type;
        this.values = values;
    }

    public int count() {
        return values.size();
    }

    public T at(int index) {
        return type.cast(values.get(index));
    }

    public Stream<T> streamOf() {
        return values.stream()
                .map(type::cast)
                .collect(toList())
                .stream();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Values<?> values1 = (Values<?>) o;
        return Objects.equals(type, values1.type) &&
                Objects.equals(values, values1.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, values);
    }
}
