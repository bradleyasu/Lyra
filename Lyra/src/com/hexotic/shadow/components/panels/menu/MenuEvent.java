package com.hexotic.shadow.components.panels.menu;

public class MenuEvent {
	
	public static final int CLOSE_MENU = 0;
	public static final int OPEN_SELECTED_LOG = 1; 
	
	
	private Object menuObject;
	private int event;
	
	public MenuEvent(int event, Object o){
		this.menuObject = o;
		this.event = event;
	}
	
	public Object getMenuObject(){
		return menuObject;
	}
	
	public int getMenuEventType() {
		return event;
	}
}
