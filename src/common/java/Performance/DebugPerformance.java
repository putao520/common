package common.java.Performance;

import common.java.Time.TimeHelper;

public class DebugPerformance {
	private long starttime;
	private final String taskName;
	private final TimeHelper timeHelper = TimeHelper.build();

	public DebugPerformance() {
		taskName = "";
		starttime = timeHelper.nowMillis();
	}

	public DebugPerformance(String _taskName) {
		taskName = _taskName;
		starttime = timeHelper.nowMillis();
	}

	public long end() {
		long rs = (timeHelper.nowMillis() - starttime);
		System.out.println("TaskName:" + taskName + "timeElapsed:" + rs);
		starttime = timeHelper.nowMillis();
		return rs;
	}
}
