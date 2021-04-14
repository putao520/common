package common.java.Thread;

import common.java.nLogger.nLogger;

public class ThreadHelper extends Thread {
	public ThreadHelper() {
		super();
	}

	public ThreadHelper(Runnable target) {
		super(target);
	}

	public ThreadHelper(String name) {
		super(name);
	}

	public ThreadHelper(Runnable target, String name) {
		super(target, name);
	}

	public ThreadHelper(ThreadGroup group, Runnable target) {
		super(group, target);
	}

	public ThreadHelper(ThreadGroup group, String name) {
		super(group, name);
	}

	public ThreadHelper(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
	}

	public ThreadHelper(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, target, name, stackSize);
	}

	public static void sleep(long millisSecond) {
		try {
			Thread.sleep(millisSecond);
		} catch (Exception e) {
			// TODO: handle exception
			nLogger.logInfo(e);
		}
	}

	public void start() {
		super.start();
	}
}
