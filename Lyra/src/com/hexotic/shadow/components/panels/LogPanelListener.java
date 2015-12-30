package com.hexotic.shadow.components.panels;

import com.hexotic.shadow.components.controls.LogLine;

public interface LogPanelListener {

	public static final int LINE_REMOVED = 0;
	public static final int LINE_APPENDED = 1;
	public static final int LINE_BOOKMARKED = 2;
	public static final int LINE_UNBOOKMARKED = 3;
	
	public void lineEvent(int event, LogLine line);
}
