package com.hexotic.shadow.components.panels;

import com.hexotic.shadow.components.controls.LogLine;

public class LogPanelEvent {

	public static final int LINE_REMOVED = 0;
	public static final int LINE_APPENDED = 1;
	public static final int LINE_BOOKMARKED = 2;
	public static final int LINE_UNBOOKMARKED = 3;
	public static final int HOTKEY_FIND = 4;
	public static final int HOTKEY_CLOSE = 5;
	
	private int event;
	private LogLine line;
	
	public LogPanelEvent(int event, LogLine line) {
		this.event = event;
		this.line = line;
	}
	
	public LogPanelEvent(int event){
		this.event = event;
	}
	
	public int getEvent(){
		return event;
	}
	
	public LogLine getLine() {
		return line;
	}
}
