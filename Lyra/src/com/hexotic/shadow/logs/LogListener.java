package com.hexotic.shadow.logs;

public interface LogListener {

	public static final int APPEND = 0;
	public static final int INIT = 1;
	public static final int NOT_FOUND = 2;
	public static final int LOG_CLOSED = 3;
	public static final int MAKE_ACTIVE = 4;
	
	public void logEvent(String logId, String line, int event, String flag);
	
}
