package com.hexotic.shadow.components.panels.footer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class FooterCounter extends JPanel{

	public static final int LEFT_CLICK = 0;
	public static final int RIGHT_CLICK = 1;
	
	private Color color;
	private int count;
	private boolean hovering = false;
	private List<FooterCounterListener> listeners;
	private String flagType;
	
	public FooterCounter(Color color, int startCount, String flagType){ 
		this.count = startCount;
		this.color = color;
		this.listeners = new ArrayList<FooterCounterListener>();
		this.flagType = flagType;
		this.setPreferredSize(new Dimension(Constants.FOOTER_COUNTER_WIDTH, Constants.FOOTER_SIZE));
		this.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				hovering = true;
				refresh();
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				hovering = false;
				refresh();
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent click) {
				int cType = RIGHT_CLICK;
				if(SwingUtilities.isLeftMouseButton(click)){
					cType = LEFT_CLICK;
				}
				for(FooterCounterListener listener : listeners) {
					listener.counterActivated(cType, getThis());
				}
			}
		});
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public String getFlagType() {
		return flagType;
	}
	
	public void addFooterCounterListener(FooterCounterListener listener){
		listeners.add(listener);
	}
	
	private FooterCounter getThis() {
		return this;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count){
		this.count = count;
	}
	
	private void refresh(){
		revalidate();
		repaint();
	}

	public void decrement() {
		if(getCount() > 0){
			setCount(getCount() - 1);
		}
		refresh();
	}
	
	public void increment() {
		setCount(getCount() + 1);
		refresh();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if(hovering){
			g2d.setColor(color.darker());
		} else {
			g2d.setColor(color);
		}
		

		g2d.fillRect(0, 0, getWidth(), getHeight());

		
		
		Font font = Theme.FOOTER_FONT.deriveFont(Theme.FOOTER_FONT_SIZE).deriveFont(Font.BOLD);
		
		FontMetrics metrics = g2d.getFontMetrics(font);
		int fht = metrics.getHeight();
		int fwd = metrics.stringWidth(String.valueOf(count));

		g2d.setColor(Theme.FOOTER_BACKGROUND);
		g2d.setFont(font);
		if(count > 0){
			g2d.drawString(String.valueOf(count), getWidth()/2-fwd/2, getHeight()/2+fht/3);
		}
		
//		g2d.setColor(Theme.FOOTER_BACKGROUND_DARKER);
//		g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
	}
}
