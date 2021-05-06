package common.java.Concurrent;

import common.java.Thread.ThreadHelper;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * 多线程安全无锁对象交换容器类
 */
public class Concurrent<T> {
    // 只写 数据存储对象
    private final AtomicReference<T> writeOnlyStore;
    /**
     * 交换区为空时->数据发生改变->{交换区状态为0时}->{交换区状态设置为1}->写入数据到交换区->{交换区状态设置为2}
     * 读取数据时->{交换区状态为2}->读取交换区数据后清空交换区->{交换区状态设置为0}
     */
    private final AtomicInteger swapSigned = new AtomicInteger();
    // 只写数据脏状态 0:干净, 1:脏
    private final AtomicInteger dirtySigned = new AtomicInteger();
    // 交换信号态
    /**
     * 0:无动作
     * 1:数据正在被写入交换区
     * 2:交互区数据等待交换
     * 3:数据正在被读出交换区
     */
    // 交换流程
    // 写入锁 0:无状态, 1:正在写入
    private final AtomicInteger writeSigned = new AtomicInteger();
    // 只读 数据存储对象
    private T readOnlyStore;
    // 交换 数据存储对象
    private T swapStore;
    private SwapFunc<T> replace_func = (r, s) -> this.readOnlyStore = s;

    private Concurrent(T v) {
        this.readOnlyStore = v;
        this.writeOnlyStore = new AtomicReference(v);
        this.swapStore = null;
        this.swapSigned.set(0);
        this.dirtySigned.set(0);
        this.writeSigned.set(0);
    }

    public static <V> Concurrent<V> build(V v) {
        return new Concurrent<V>(v);
    }

    public Concurrent<T> setReplaceFunc(SwapFunc<T> func) {
        this.replace_func = func;
        return this;
    }

    public T get() {
        // 如果 swap 为 2,替换 write 数据到 read
        if (swapSigned.compareAndSet(2, 3)) {
            this.replace_func.swap(this.readOnlyStore, this.swapStore);
            // 设置写入区为干净
            dirtySigned.set(0);
            // 交换区数据已经全部读完,设置交换区为可用
            swapSigned.compareAndSet(3, 0);
        }
        return this.readOnlyStore;
    }

    // 替换写入对象
    public Concurrent<T> set(T v) {
        this.writeOnlyStore.set(v);
        return this;
    }

    private boolean tryGetWriter() {
        return (writeSigned.compareAndExchange(0, 1) == 0);
    }

    private void getWriter() {
        while (!tryGetWriter()) {
            ThreadHelper.sleep(10);
        }
    }

    private void freeWriter() {
        writeSigned.set(0);
    }

    // 写入对象是一个类
    public boolean set(Function<T, T> write_func) {
        // 等待写入锁为 0
        if (!tryGetWriter()) {
            return false;
        }
        // 写入数据
        write_func.apply(this.writeOnlyStore.get());
        // 设置数据为脏
        dirtySigned.set(1);
        // 写入锁为 0
        freeWriter();
        return true;
    }

    public Concurrent<T> setSpin(Function<T, T> write_func) {
        while (!set(write_func)) {
            ThreadHelper.sleep(10);
        }
        return this;
    }

    public boolean flush() {
        // 等待交换区锁为 0
        if (swapSigned.compareAndExchange(0, 1) != 0) {
            return false;
        }
        // 获得写入锁
        getWriter();
        // 写入数据到交换区
        this.swapStore = this.writeOnlyStore.get();
        // 释放写入锁
        freeWriter();
        // 设置交换区锁为 2（等待交换）
        swapSigned.incrementAndGet();
        return true;
    }

    public Concurrent<T> flushSpin() {
        while (!flush()) {
            ThreadHelper.sleep(10);
        }
        return this;
    }

    // 是否有新鲜数据需要推送
    public boolean hasFresh() {
        return dirtySigned.intValue() == 1;
    }
}
