package com.hexotic.shadow.components.panels.footer;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class FooterMenuItem extends JPanel{

	private boolean selected = false;
	private List<FooterMenuItemListener> listeners;
	private Image icon;
	
	public FooterMenuItem(Image icon){
		this.icon = icon;
		listeners = new ArrayList<FooterMenuItemListener>();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.setPreferredSize(new Dimension(Constants.FOOTER_COUNTER_WIDTH*2, Constants.FOOTER_SIZE));
		
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				click();
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		
		updateBackground();
	}
	
	public void click() {
		selected = !selected;
		notifyListeners();
		updateBackground();
	}
	
	private void notifyListeners(){
		for(FooterMenuItemListener listener : listeners) {
			listener.itemActivated(selected);
		}
	}
	
	private void updateBackground() {
		if(selected){
			this.setBackground(Theme.FOOTER_BACKGROUND_DARKER);
		} else {
			this.setBackground(Theme.FOOTER_BACKGROUND);
		}
	}
	
	public void  addFooterMenuItemListener(FooterMenuItemListener listener){
		listeners.add(listener);
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(icon, 0, getHeight()/2 - icon.getHeight(null)/2, null);
	}
}
