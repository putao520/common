package common.java.EventWorker;

public class SafeEvent {
    public final Object func;
    public final int safeCode;

    public SafeEvent(int safeCode, Object func) {
        this.func = func;
        this.safeCode = safeCode;
    }
}
