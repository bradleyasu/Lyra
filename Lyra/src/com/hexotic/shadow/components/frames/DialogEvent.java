package com.hexotic.shadow.components.frames;

import java.io.File;

public class DialogEvent {

	public static final int CANCELLED = -1;
	public static final int NO = 0;
	public static final int YES = 1;
	public static final int FILE_SUBMIT = 3;
	
	private int event;
	private File fileSubmit;
	
	public DialogEvent(int event, File fileSubmit) {
		this.event = event;
		this.fileSubmit = fileSubmit;
	}
	
	
	public DialogEvent(int event) {
		this.event = event;
	}
	
	public File getFile() {
		return fileSubmit;
	}
	
	public int getEvent(){
		return event;
	}
}
