package com.hexotic.shadow.constants;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import com.hexotic.lib.resource.Resources;

public class Theme {

	/* Primary Theme Colors */
	public static Color MAIN_BACKGROUND = new Color(0xffffff);
	public static Color SIDEBAR_BACKGROUND = new Color(0xf4f4f4); 
	
	public static Color LINE_NUMBER_COLOR = new Color(0x888888);
	public static Color LINE_NUMBER_BACKGROUND = new Color(0xe8e8e8);
	public static int   LINE_NUMBER_WIDTH = 20;
	public static Color LINE_BACKGROUND = new Color(0xffffff);
	public static Color LINE_FOREGROUND = new Color(0x242424);

	public static Color LINE_SELECT_COLOR = new Color(0x00b0ff);
	public static Color LINE_HOVER_COLOR = new Color(0xf1f1f1);
	
	/* Alert Colors */
	public static Color ERROR_COLOR = new Color(0xFF8A80);
	public static Color WARNING_COLOR = new Color(0xFFEA00);
	
	private static Theme instance = new Theme();
	
	
	public static Font LINE_FONT;
	public static float LINE_FONT_SIZE = (float) 12.0;
	
	private Theme() {
		loadDefaultFonts();
	}
	
	private void loadDefaultFonts() {
		try {
			LINE_FONT = Resources.getInstance().getFont("Inconsolata-Regular.ttf").deriveFont(LINE_FONT_SIZE);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setLineNumberWidth(int width) {
		LINE_NUMBER_WIDTH = width;
	}
	
	public static Theme getTheme(){ 
		return instance;
	}
}
