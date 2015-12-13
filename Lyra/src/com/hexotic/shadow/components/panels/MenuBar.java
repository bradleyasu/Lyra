package com.hexotic.shadow.components.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class MenuBar extends JPanel{

	private boolean showShadow = false;
	
	public MenuBar() {
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.setPreferredSize(new Dimension(Constants.MENUBAR_WIDTH, Constants.MENUBAR_HEIGHT));
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
	}
	
	public void setShadow(boolean showShadow){
		this.showShadow = showShadow;
	}
	
	@Override
	public void paintComponent(Graphics g){ 
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(showShadow){
			g2d.setPaint(new GradientPaint(0,getHeight()-3, new Color(0x424242), 0, getHeight(), Theme.MAIN_BACKGROUND));
			g2d.fillRect(0,getHeight()-3,getWidth(),getHeight());
		}
	}
}
