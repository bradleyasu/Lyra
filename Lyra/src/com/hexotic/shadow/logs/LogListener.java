package com.hexotic.shadow.logs;

public interface LogListener {

	//public void logEvent(String logId, String line, int event, String flag);
	
	public void lineAppended(LogEvent event);
	public void logClosed(String logId);
	public void logOpened(String logId);
	public void logActivated(String logId);
	public void logDeactivated(String logId);
	public void logNotFound(String logId);
	
}
