package com.hexotic.shadow.components.panels;

public class LogPanelEvent {

	public static final int LINE_REMOVED = 0;
	public static final int LINE_APPENDED = 1;
	public static final int LINE_BOOKMARKED = 2;
	public static final int LINE_UNBOOKMARKED = 3;
	public static final int HOTKEY_FIND = 4;
	public static final int HOTKEY_CLOSE = 5;
	public static final int HOTKEY_SHADOW_MENU = 6;
	public static final int ACTIVATE_LOG = 7;
	public static final int DEACTIVATE_LOG = 8;
	
	private int event;
	private Object eventObject;
	
	public LogPanelEvent(int event, Object eventObject) {
		this.event = event;
		this.eventObject = eventObject;
	}
	
	public LogPanelEvent(int event){
		this.event = event;
	}
	
	public int getEvent(){
		return event;
	}
	
	public Object getEventObj() {
		return eventObject;
	}
}
