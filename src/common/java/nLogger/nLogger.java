package common.java.nLogger;

public class nLogger {
	private static boolean isDebug = false;
	public static LoggerOut clientFunc = null;

	public static void onLogger(LoggerOut event) {
		clientFunc = event;
	}

	public static void setDebug(boolean state) {
		isDebug = state;
	}

	private static void out(String info, LogInfo.InfoType type) {
		if (clientFunc != null) {
			clientFunc.out(info, type);
		}
	}

	private static String getLogInfo(Exception e, LogInfo.InfoType type, String in) {
		LogInfo infoObj = e == null ? LogInfo.build() : LogInfo.build(e);
		return infoObj.level(LogInfo.InfoType.DEBUG)
				.info(in)
				.toString();
	}

	public static void debugInfo(Exception e) {
		debugInfo(e, null);
	}

	public static void debugInfo(String in) {
		debugInfo(null, in);
	}
	public static void debugInfo(Exception e, String in){
		if( !isDebug ){
			return;
		}
		String info = getLogInfo(e, LogInfo.InfoType.DEBUG, in);
		System.out.println(info);
		out(info, LogInfo.InfoType.DEBUG);
	}
	public static void logInfo(Exception e){
		logInfo(e, null);
	}
	public static void logInfo(String in){
		logInfo(null, in);
	}
	public static void logInfo(Exception e,String in){
		String info = getLogInfo(e, LogInfo.InfoType.LOG, in);
		if( isDebug ){
			System.out.println(info);
		}
		out(info, LogInfo.InfoType.DEBUG);
	}
	public static void warnInfo(Exception e){
		warnInfo(e, null);
	}
	public static void warnInfo(String in){
		warnInfo(null, in);
	}
	public static void warnInfo(Exception e,String in){
		System.out.println(getLogInfo(e, LogInfo.InfoType.LOG, in));
	}
	public static void errorInfo(Exception e){
		warnInfo(e, null);
	}
	public static void errorInfo(String in){
		warnInfo(null, in);
	}
	public static void errorInfo(Exception e,String in){
		String info = getLogInfo(e, LogInfo.InfoType.ERROR, in);
		System.out.println(info);
		out(info, LogInfo.InfoType.ERROR);
	}
}