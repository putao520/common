package common.java.worker;

import common.java.eventBus.EventBus;
import common.java.nlogger.nlogger;

public class Worker {
    public static final void submit(Runnable func) {
        if( func != null ){
            try {
                EventBus.event(func);
            } catch (InterruptedException e) {
                nlogger.logInfo(e);
            }
        }
    }
}
