package kungzhi.muse.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.reflect.Array.newInstance;

public class Values<T>
        implements Serializable {
    private final Class<T> type;
    private final Object[] values;

    public Values(Class<T> type, Object[] values) {
        this.type = type;
        this.values = values;
    }

    public int count() {
        return values.length;
    }

    public T at(int index) {
        return type.cast(values[index]);
    }

    public Stream<T> streamOf() {
        if (type != Object.class) {
            T[] typedArray = (T[]) newInstance(type, values.length);
            System.arraycopy(values, 0, typedArray, 0, values.length);
            return Stream.of(typedArray);
        }
        return Stream.of((T[]) values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Values<?> values1 = (Values<?>) o;

        if (type != null ? !type.equals(values1.type) : values1.type != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(values, values1.values);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }

    @Override
    public String toString() {
        return "Values{" +
                "type=" + type.getSimpleName() +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
