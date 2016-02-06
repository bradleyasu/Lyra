package com.hexotic.shadow.constants;

public class Constants {

	/* Software information */
	public static final String APP_NAME = "Shadow";
	public static final String APP_COMPANY = "Hexotic Software";
	public static final String VERSION = "1.0.0";
	
	/* Main window properties and attributes */
	public static final int WIDTH = 875;
	public static final int HEIGHT = 600;
	
	public static final boolean SHOW_HORIZONTAL_SCROLL = false;
	
	/* Sidebar attributes */
	public static final int SIDEBAR_WIDTH = 320;
	public static final int SIDEBAR_HEIGHT = HEIGHT;
	
	/* Menubar attributes */
	public static final int MENUBAR_WIDTH = WIDTH;
	public static final int MENUBAR_HEIGHT = 0;
	
	public static final int X_OFFSET = 16;
	public static final int Y_OFFSET = 39;

	/* Flag Configuration Window Attributes */
	public static final int FLAG_CONFIG_WIDTH = 400;
	public static final int FLAG_CONFIG_HEIGHT = 80;
	
	/* Footer attributes */
	public static final int FOOTER_SIZE = 20;
	public static final int FOOTER_COUNTER_WIDTH = 50;
	/* Log settings */
	public static final int LOG_MAX = 2000;
	public static final int REFRESH_RATE = 100; // Milliseconds
	
	public static final String CONFIG_DIR = System.getenv("APPDATA")  +"\\Shadow";
	public static final String SSH_DIR = System.getenv("APPDATA") +"\\Shadow\\connections";
}
