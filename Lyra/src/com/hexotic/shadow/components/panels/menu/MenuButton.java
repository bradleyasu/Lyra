package com.hexotic.shadow.components.panels.menu;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import com.hexotic.lib.util.StringOps;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class MenuButton extends JPanel{
	
	private Font font;
	private String text;
	private String id;
	private Color background;
	private boolean isActive = false;
	
	public MenuButton(String id, String text) {
		this.text = text.toLowerCase();
		this.id = id;
		this.background = Theme.FOOTER_BACKGROUND_DARKER;
		this.setPreferredSize(new Dimension(Constants.SIDEBAR_WIDTH/3, 30));
		font = Theme.FOOTER_FONT.deriveFont((float)14.0).deriveFont(Font.BOLD);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	public String getId() {
		return id;
	}
	
	public void setActive(boolean active) {
		this.isActive = active;
	}
	
	public void setupMouseListener(final MenuControlPanel controlPanel) {
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				background = Theme.FOOTER_BACKGROUND;
				refresh();
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				background = Theme.FOOTER_BACKGROUND_DARKER;
				refresh();
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				controlPanel.setActive(getId());
			}
		});
	}
	
	private void refresh() {
		revalidate();
		repaint();
	}
	

    @Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(isActive){
			g2d.setColor(Theme.FOOTER_BACKGROUND);
		} else {
			g2d.setColor(background);
		}
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		Dimension size = StringOps.getStringBounds(g2d, font, text);
		
		g2d.setColor(Theme.FOOTER_FONT_COLOR);
		g2d.setFont(font);
		g2d.drawString(text, getWidth()/2-size.width/2, size.height);
		
		g2d.setColor(Theme.FOOTER_BACKGROUND);
		g2d.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
		
    }
}
