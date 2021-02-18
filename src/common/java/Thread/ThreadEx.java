package common.java.Thread;

import common.java.nLogger.nLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreadEx extends Thread {
	private List<Object> _parameters;
	{
		_parameters = new ArrayList<>();
	}
	public ThreadEx( ){
		super();
	}
	public ThreadEx(Runnable target){
		super(target);
	}
	public ThreadEx(String name){
		super(name);
	}
	public ThreadEx(Runnable target, String name){
		super(target,name);
	}
	public ThreadEx(ThreadGroup group, Runnable target){
		super(group,target);
	}
	public ThreadEx(ThreadGroup group, String name){
		super(group,name);
	}
	public ThreadEx(ThreadGroup group, Runnable target, String name){
		super(group,target,name);
	}
	public ThreadEx(ThreadGroup group, Runnable target, String name, long stackSize){
		super(group,target,name,stackSize);
	}
	public void start(Object ...parameter){
		_parameters = Arrays.asList(parameter);
		super.start();
	}

	public static final void SleepEx(long millisSecond) {
		try {
			Thread.sleep(millisSecond);
		}
		catch (Exception e) {
			// TODO: handle exception
			nLogger.logInfo(e);
		}
	}
}