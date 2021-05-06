package common.java.Concurrent;

@FunctionalInterface
public interface SwapFunc<T> {
    void swap(T read, T swap);
}
