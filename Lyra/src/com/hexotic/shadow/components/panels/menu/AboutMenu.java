package com.hexotic.shadow.components.panels.menu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class AboutMenu extends JPanel{

	private Image logo;
	private Image header;
	private Image donate;
	private Image hexotic;
	private Image feedback;
	
	public AboutMenu(){
		this.setBackground(Theme.FOOTER_BACKGROUND);
		

		
		loadResources();
	}

	private void loadResources() {
		try {
			header = Resources.getInstance().getImage("about/about_header.png");
			feedback = Resources.getInstance().getImage("about/about_feedback.png");
			hexotic = Resources.getInstance().getImage("about/about_hexotic.png");
			donate = Resources.getInstance().getImage("about/about_donate.png");
		} catch (ResourceException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int x = 0;
		int y = 0;
		g2d.drawImage(header, x, y, null);
		y = y + header.getHeight(null);
		
		g2d.drawImage(donate, x, y, null);
		y = y + donate.getHeight(null);
		
//		g2d.drawImage(hexotic, x, y, null);
//		y = y + hexotic.getHeight(null);
//		
		g2d.drawImage(feedback, x, y, null);
		y = y + feedback.getHeight(null);
		
		g2d.setColor(Theme.FOOTER_FONT_COLOR);
		g2d.drawString("Version: "+Constants.VERSION, 130, 200);
	}
}
