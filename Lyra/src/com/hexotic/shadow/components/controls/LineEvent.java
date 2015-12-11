package com.hexotic.shadow.components.controls;

public class LineEvent {

	public static final int OTHER = -1;
	public static final int PRESSED = 0;
	public static final int RELEASE = 1;
	public static final int DRAGSELECTED = 2;
	public static final int DRAGDESELECTED = 3;
	
	private int lineNumber;
	private int event = -1;
	
	public LineEvent(int lineNumber, int event) {
		this.lineNumber = lineNumber;
		this.event = event;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public int getEvent() {
		return event;
	}
}
