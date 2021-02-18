package common.java.EventWorker;

import common.java.nLogger.nLogger;

public class Worker {
    public static final void submit(Runnable func) {
        if( func != null ){
            try {
                EventBus.event(func);
            } catch (InterruptedException e) {
                nLogger.logInfo(e);
            }
        }
    }
}
