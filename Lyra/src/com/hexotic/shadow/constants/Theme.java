package com.hexotic.shadow.constants;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.hexotic.lib.resource.Resources;

public class Theme {

	/* Primary Theme Colors */
	public static Color MAIN_BACKGROUND = new Color(0xBDC3C7);
	public static Color SIDEBAR_BACKGROUND = new Color(0xf4f4f4);
	public static Color SHADOW_COLOR = new Color(0x3b3b3b);
	
	public static Color LINE_NUMBER_COLOR = new Color(0x2C3E50);
	public static Color LINE_NUMBER_BACKGROUND = new Color(0xBDC3C7);
	public static int   LINE_NUMBER_WIDTH = 20;
	public static Color LINE_BACKGROUND = new Color(0xECF0F1);
	public static Color LINE_FOREGROUND = new Color(0x0A152B);

	public static Color LINE_SELECT_COLOR = new Color(0x9B59B6);
	public static Color LINE_BOOKMARK_COLOR = new Color(0x8E44AD);
	public static Color LINE_HOVER_COLOR = new Color(0xBDC3C7);
	
	public static Color LINE_FILTER_COLOR = new Color(0x9B59B6);

	
	/* Footer colors */
	public static Color FOOTER_BACKGROUND = new Color(0x34495E);
	public static Color FOOTER_BACKGROUND_DARKER = FOOTER_BACKGROUND.darker();
	public static Font FOOTER_FONT;
	public static float FOOTER_FONT_SIZE = (float) 12.0;
	public static Color FOOTER_FONT_COLOR = new Color(0xECF0F1);
	
	
	/* Alert Colors */
	public static Color INFO_COLOR = new Color(0x3498DB);
	public static Color SUCCESS_COLOR = new Color(0x2ECC71);
	public static Color ERROR_COLOR = new Color(0xE74C3C);
	public static Color WARNING_COLOR = new Color(0xF1C40F);
	
	private static Theme instance = new Theme();
	
	
	public static Font LINE_FONT;
	public static float LINE_FONT_SIZE = (float) 12.0;
	
	private Theme() {
		loadDefaultFonts();
	}
	
	private void loadDefaultFonts() {
		try {
			LINE_FONT = Resources.getInstance().getFont("Inconsolata-Regular.ttf").deriveFont(LINE_FONT_SIZE);
			FOOTER_FONT = Resources.getInstance().getFont("Oxygen/Oxygen-Regular.ttf").deriveFont(FOOTER_FONT_SIZE);
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
