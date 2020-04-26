package common.java.eventBus;
/**
 * 1:实现数据过滤
 * 2:实现数据达到路由分发
 * 3:实现操作订阅
 * 4:提供对应核心服务，做到网络投递事件
 */

import common.java.string.StringHelper;
import org.json.simple.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.function.Supplier;

public class EventBus {
    private static final int maxDeep = 2048;
    private static BlockingQueue<Object> queue;
    private static JSONObject subscribe;
    private static ExecutorService runner;

    static {
        subscribe = new JSONObject();
        queue = new LinkedBlockingQueue<>(maxDeep);
        int availableNo = Runtime.getRuntime().availableProcessors() / 2;
        runner = Executors.newFixedThreadPool(availableNo);
        for (int i = 0; i < availableNo; i++) {
            runner.submit(() -> {
                while (true) {
                    Object msg = queue.take();
                    if (msg instanceof EventGrape) {//是订阅触发
                        EventGrape eg = (EventGrape) msg;
                        String eName = eg.getEventName();
                        if (subscribe.containsKey(eName)) {//是否已经订阅
                            Object param = eg.getEventParam();
                            //Object result = null;
                            SafeEvent se = (SafeEvent) subscribe.get(eName);
                            if (param == null) {
                                Supplier<Object> func = (Supplier<Object>) se.func;
                                func.get();
                            } else {
                                Function<Object, Object> func = (Function<Object, Object>) se.func;
                                func.apply(param);
                            }
                        }
                    } else if (msg instanceof Supplier) {
                        ((Supplier) msg).get();
                    } else if (msg instanceof Runnable) {
                        ((Runnable) msg).run();
                    }
                }
            });
        }
    }

    /**
     * 判断该订阅是否已经存在
     * */
    public static final boolean contain(String eventName) {
        return subscribe.containsKey(eventName);
    }

    public static <T extends Function> int listen(String eventName, T func) {
        return addListen(eventName, func);
    }

    public static <T extends Supplier> int listen(String eventName, T func) {
        return addListen(eventName, func);
    }

    public static <T extends Runnable> int listen(String eventName, T func) {
        return addListen(eventName, func);
    }

    /**
     * 添加订阅，返回事件安全码，用来删除时所有权效验
     * */
    private static int addListen(String eventName, Object func) {
        int r = 0;
        if (!contain(eventName)) {
            r = StringHelper.createRandomCode(8).hashCode();
            subscribe.put(eventName, new SafeEvent(r, func));
        }
        return r;
    }

    /**
     * 删除订阅
     * */
    public static boolean remove(String eventName, int safeCode) {
        boolean r = false;
        if (contain(eventName)) {
            SafeEvent se = (SafeEvent) (subscribe.get(eventName));
            if (se.safeCode == safeCode) {
                subscribe.remove(eventName);
                r = true;
            }
        }
        return r;
    }

    /**
     * 压入立刻事件
     * */
    public static <T> void event(Supplier<T> func) throws InterruptedException {
        queue.put(func);
    }

    /**
     * 压入立刻事件
     * */
    public static void event(Runnable func) throws InterruptedException {
        queue.put(func);
    }

    /***
     * 压入订阅事件
     */
    public static void event(String eventName) throws InterruptedException {
        queue.put(new EventGrape(eventName));
    }

    /***
     * 压入订阅事件
     */
    public static void event(String eventName, Object eventParam) throws InterruptedException {
        queue.put(new EventGrape(eventName, eventParam));
    }
}
