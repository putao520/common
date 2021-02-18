package common.java.nLogger;

@FunctionalInterface
public interface LoggerOut {
    void out(String info, LogInfo.InfoType type);
}
