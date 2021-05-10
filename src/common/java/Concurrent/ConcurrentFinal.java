package common.java.Concurrent;

public class ConcurrentFinal<V> {
    private final V v;

    private ConcurrentFinal(V v) {
        this.v = v;
    }

    public static <V> ConcurrentFinal<V> build(V v) {
        return new ConcurrentFinal<V>(v);
    }

    public V set() {
        return v;
    }
}
