package com.hexotic.shadow.components.panels.menu;


public class MenuItemEvent {

	public static final int CLOSE_EVENT = -1;
	
	private int event;
	private ShadowMenuItem source;
	
	public MenuItemEvent(ShadowMenuItem source, int eventId){
		this.source = source;
		this.event = eventId;
	}
	
	public ShadowMenuItem getSource() {
		return source;
	}
	
	public int getEventId() {
		return event;
	}
	
}
