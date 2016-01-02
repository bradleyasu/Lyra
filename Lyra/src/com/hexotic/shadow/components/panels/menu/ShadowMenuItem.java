package com.hexotic.shadow.components.panels.menu;

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
import com.hexotic.shadow.logs.Log;
import com.hexotic.shadow.logs.LogEvent;
import com.hexotic.shadow.logs.LogListener;

public class ShadowMenuItem extends JPanel{

	private Log log;
	private int lineCount = 1;
	private Font lgFont;
	private Font mdFont;
	private Font smFont;
	private boolean isHovering = false;
	private boolean isActive = false;
	
	public ShadowMenuItem(Log l) {
		this.setPreferredSize(new Dimension(Constants.SIDEBAR_WIDTH-5, 100));
		this.setMinimumSize(this.getPreferredSize());
		this.setMaximumSize(this.getPreferredSize());
		this.log = l;
		
		log.addLogListener(new LogListener(){

			@Override
			public void lineAppended(LogEvent event) {
				lineCount++;
			}

			@Override
			public void logClosed(String logId) {
				remove();
			}

			@Override
			public void logOpened(String logId) {
				lineCount = 0;
			}

			@Override
			public void logActivated(String logId) {
				isActive = true;
			}

			@Override
			public void logNotFound(String logId) {
				lineCount = -1;
				
			}

			@Override
			public void logDeactivated(String logId) {
				isActive = false;
			}
		});
		
		smFont = Theme.FOOTER_FONT.deriveFont((float)8.0);
		mdFont = Theme.FOOTER_FONT.deriveFont((float)12.0);
		lgFont = Theme.FOOTER_FONT.deriveFont((float)16.0).deriveFont(Font.BOLD);
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		bindMouseListener();
	}

	private void bindMouseListener() {
		this.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				isHovering = true;
				refresh();
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				isHovering = false;
				refresh();
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				log.activate();
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
	}
	
	public void remove() {
		
	}
	
	public String getFileName() {
		return log.getFile().getName();
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
		
		int imgWidth = 80;
		if(isHovering){
			g2d.setColor(Theme.FOOTER_BACKGROUND.brighter());
		} else{
			g2d.setColor(Theme.FOOTER_BACKGROUND);
		}
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		g2d.setColor(Theme.FOOTER_BACKGROUND.brighter());
		g2d.drawLine(0,0,getWidth(), 0);
		
		g2d.setColor(Theme.FOOTER_BACKGROUND.darker());
		g2d.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
		
		g2d.setColor(Theme.FOOTER_FONT_COLOR);
		g2d.setFont(lgFont);
		g2d.drawString(log.getFile().getName(), imgWidth, 35);
		
		g2d.setFont(mdFont);
		if(lineCount >= 0){
			g2d.drawString("Lines: "+lineCount, imgWidth, 55);
		} else {
			g2d.setColor(Theme.ERROR_COLOR);
			g2d.drawString("File Moved, Missing, or Invalid file", imgWidth, 55);
		}

		g2d.setFont(smFont);
		Dimension size = StringOps.getStringBounds(g2d, smFont, log.getLogId());
		g2d.setColor(Theme.FOOTER_BACKGROUND.brighter());
		g2d.drawString(log.getLogId(), getWidth()-size.width, getHeight()-5);
		
		if(log.isActivated()){
			g2d.setColor(Theme.FOOTER_FONT_COLOR);
			g2d.fillRect(0,0,5,getHeight()-2);
		}
	}
}
