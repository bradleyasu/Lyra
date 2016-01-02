package com.hexotic.shadow.components.panels.menu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;
import com.hexotic.shadow.logs.Log;
import com.hexotic.shadow.logs.LogListener;

public class ShadowMenuItem extends JPanel{

	private Log log;
	private int lineCount = 0;
	
	public ShadowMenuItem(Log l) {
		this.setPreferredSize(new Dimension(Constants.SIDEBAR_WIDTH-5, 100));
		this.setMinimumSize(this.getPreferredSize());
		this.setMaximumSize(this.getPreferredSize());
		
		this.log = l;
		
		log.addLogListener(new LogListener(){
			@Override
			public void lineAppeneded(String logId, String line, int event, String flag) {
				if(event == LogListener.INIT){
					lineCount = 0;
				} else if(event == LogListener.APPEND){
					lineCount++;
				}
				refresh();
			}
		});
	}
	
	private void refresh(){
		revalidate();
		repaint();
	}
	
	public Log getLog() {
		return log;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		g2d.setColor(Theme.FOOTER_BACKGROUND);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		g2d.setColor(Theme.FOOTER_BACKGROUND.brighter());
		g2d.drawLine(0,0,getWidth(), 0);
		
		g2d.setColor(Theme.FOOTER_BACKGROUND.darker());
		g2d.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
		
		g2d.setColor(Theme.FOOTER_FONT_COLOR);
		g2d.drawString(log.getFile().getName(), 20, 20);
		g2d.drawString("Lines: "+lineCount, 20, 50);
		
		g2d.setColor(Theme.FOOTER_BACKGROUND.brighter());
		g2d.drawString(log.getLogId(), 10, getHeight()-5);
	}
}
