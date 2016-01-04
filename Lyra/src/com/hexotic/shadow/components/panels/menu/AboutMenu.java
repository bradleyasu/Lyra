package com.hexotic.shadow.components.panels.menu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.shadow.constants.Theme;

public class AboutMenu extends JPanel{

	private Image logo;
	
	public AboutMenu(){
		this.setBackground(Theme.FOOTER_BACKGROUND);
		
		try {
			logo = Resources.getInstance().getImage("icon_lg.png");
		} catch (ResourceException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.drawImage(logo, getWidth()/2-logo.getWidth(null)/2, getHeight()/2-logo.getHeight(null)/2, null);
		
	}
}
