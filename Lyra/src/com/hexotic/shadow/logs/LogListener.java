package com.hexotic.shadow.logs;

public interface LogListener {

	public static final int APPEND = 0;
	public static final int INIT = 1;
	
	public void lineAppeneded(String logId, String line, int event, String flag);
	
}
