package common.java.nlogger;

@FunctionalInterface
public interface LoggerOut {
    void out(String info, LogInfo.InfoType type);
}
