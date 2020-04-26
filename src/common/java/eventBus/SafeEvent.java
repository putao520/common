package common.java.eventBus;

public class SafeEvent {
    public Object func;
    public int safeCode;

    public SafeEvent(int safeCode, Object func) {
        this.func = func;
        this.safeCode = safeCode;
    }
}
