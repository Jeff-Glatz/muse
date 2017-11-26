package kungzhi.platform.runtime;

public interface ControllerFactory {
    <T> T load(Class<T> type);
    <T> T load(String name, Class<T> type);
}
