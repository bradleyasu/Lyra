package com.hexotic.shadow.components.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.hexotic.shadow.components.controls.LineEvent;
import com.hexotic.shadow.components.controls.LineListener;
import com.hexotic.shadow.components.controls.LogLine;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;
import com.hexotic.shadow.logs.Log;
import com.hexotic.shadow.logs.LogListener;

public class LogPanel extends JPanel{

	public static final int NO_PAUSE = 0;
	public static final int GROW_BUT_DONT_REMOVE =1;
	
	private TreeMap<Integer, LogLine> lines;
	
	
	private int removeNext = 0;
	private int lineCount = 0;
	private Log log;
	private LogPanel panel;
	private int mouseDownLine = -1;
	private int mouseUpLine = -1;
	
	private int pauseState = NO_PAUSE;
	
	public LogPanel() {
		panel = this;
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		lines = new TreeMap<Integer, LogLine>();
		
		bindHotkeys();
		this.setFocusable(true);
		obtainFocus();
	}
	
	public void setLog(Log log) {
		this.log = log;
		log.addLogListener(new LogListener() {
			@Override
			public void lineAppeneded(String line) {
				addLine(line);
			}
			
		});
	}
	
	public Log getLog() {
		return log;
	}
	
	private void bindHotkeys() {
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent key) {
				if(key.isControlDown() && key.getKeyCode() == key.VK_A){
					selectAll();
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});
	}

	public void selectAll() {
		for(LogLine line : lines.values()){
			line.setSelected(true);
		}
		revalidate();
		repaint();
	}
	
	public void setPauseState(int state){
		if(state == NO_PAUSE && state != pauseState){
			flushLog();
		}
		pauseState = state;
	}
	
	public void flushLog() {
		while(lines.size() > Constants.LOG_MAX){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
			      panel.remove(0);
				}
			});
			
			lines.remove(removeNext);
			removeNext++;
		}
	}
	
	public void addLine(String line) {
		LogLine logLine = new LogLine(lineCount, line);
		lines.put(lineCount, logLine);
		this.add(logLine);
		
		if(lines.size() > Constants.LOG_MAX && pauseState == NO_PAUSE){
			
			// Cleanup old lines when the JVM can handle it
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
			      panel.remove(0);
				}
			});
			
			lines.remove(removeNext);
			removeNext++;
		}
		
		logLine.addLineListener(new LineListener() {
			@Override
			public void lineAction(LineEvent event) {
				switch(event.getEvent()){
					case LineEvent.CLICKED:
						mouseDownLine = event.getLineNumber();
						mouseUpLine = mouseDownLine;
						updateSelected();
						mouseUpLine = -1;
						mouseDownLine = mouseUpLine;
						break;
					case LineEvent.PRESSED:
						mouseDownLine = event.getLineNumber();
						mouseUpLine = mouseDownLine;
						updateSelected();
						break;
					case  LineEvent.RELEASE:
						mouseUpLine = -1;
						mouseDownLine = mouseUpLine;
						obtainFocus();
						break;
					case LineEvent.ENTERED:
						if(mouseDownLine >= 0){
							mouseDownLine = event.getLineNumber();
							updateSelected();
						}
						break;
					case LineEvent.EXITED:
						break;
				}
				
				refresh();
			}
		});
		
		lineCount++;
		
		refresh();
	}
	
	public int getLineCount() {
		return lineCount;
	}
	
	private void obtainFocus() {
		this.requestFocus();
	}
	
	private void updateSelected() {
		for(int lineNumber : lines.keySet()){
			lines.get(lineNumber).setSelected(isBetween(mouseDownLine, mouseUpLine, lineNumber));
		}
		refresh();
	}
	
	private void refresh() {
		revalidate();
		repaint();
	}
	
	public boolean isBetween(int start, int end, int value) {
	    return end > start ? value >= start && value <= end : value >= end && value <= start;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(lines.isEmpty()){
			g2d.drawString("Nothing Here", 10, 10);
		}
		
	}
	

}
