package com.hexotic.shadow.components.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.hexotic.lib.ui.input.textfield.ModernTextField;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class MenuBar extends JPanel{

	public MenuBar() {
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.setPreferredSize(new Dimension(Constants.MENUBAR_WIDTH, Constants.MENUBAR_HEIGHT));
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
	}
	
	
	@Override
	public void paintComponent(Graphics g){ 
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setPaint(Theme.LINE_NUMBER_COLOR);
		g2d.drawLine(0, getHeight()-1,getWidth(), getHeight()-1);
	}
}
