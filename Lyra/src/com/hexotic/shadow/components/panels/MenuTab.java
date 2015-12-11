package com.hexotic.shadow.components.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class MenuTab extends JPanel {

	private boolean selected = true;
	private String tabName = "";
	private boolean dimConfigured = false;
	
	public MenuTab(String tabName) {
		this.tabName = tabName;
		this.setBackground(Theme.MAIN_BACKGROUND);
		//this.setPreferredSize(new Dimension(100, 50));
	}
	
	@Override
	public void paintComponent(Graphics g){ 
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Prepare Font
		Font font = Theme.LINE_FONT.deriveFont((float)15.0).deriveFont(Font.BOLD);
		g2d.setFont(font);
		
		FontMetrics metrics = g2d.getFontMetrics(font);
		int fht = metrics.getHeight();
		int fwd = metrics.stringWidth(tabName);

		if(!dimConfigured) {
			this.setPreferredSize(new Dimension(fwd+4, Constants.MENUBAR_HEIGHT));
			dimConfigured = true;
			revalidate();
		}
		
		g2d.drawString(tabName, getWidth()/2 - fwd/2, getHeight()/2 );
		
		g2d.setPaint(Theme.LINE_NUMBER_COLOR);
		g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
		
		if(selected){
			g2d.setPaint(Theme.MAIN_BACKGROUND);
			g2d.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
		}
		
		
	}
}
