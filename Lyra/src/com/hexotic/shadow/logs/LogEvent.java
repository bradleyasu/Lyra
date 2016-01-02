package com.hexotic.shadow.logs;

public class LogEvent {

	public static final int APPEND = 0;
	public static final int INIT = 1;
	public static final int NOT_FOUND = 2;
	public static final int LOG_CLOSED = 3;
	public static final int MAKE_ACTIVE = 4;
	
	private String logId;
	private String line;
	private int event;
	private String flag;
	
	public LogEvent(String logId, String line, int event, String flag){
		this.logId = logId;
		this.line = line;
		this.event = event;
		this.flag = flag;
	}
	
	public String getLogId() {
		return logId;
	}
	
	public String getLine(){
		return line;
	}
	
	public int getEvent() {
		return event;
	}
	
	public String getFlag() {
		return flag;
	}
}
