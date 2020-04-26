package common.java.performance;

import common.java.time.TimeHelper;

public class DebugPerformance {
	private long starttime;
	private String taskName;
	private TimeHelper timeHelper = TimeHelper.build();
	public DebugPerformance(){
		taskName = "";
        starttime = timeHelper.nowMillis();
	}
	public DebugPerformance(String _taskName){
		taskName = _taskName;
        starttime = timeHelper.nowMillis();
	}
	public long end(){
        long rs = (timeHelper.nowMillis() - starttime);
		System.out.println( "TaskName:" + taskName + "timeElapsed:" + rs );
        starttime = timeHelper.nowMillis();
		return rs;
	}
}
