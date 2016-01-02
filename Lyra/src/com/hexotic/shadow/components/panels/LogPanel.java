package com.hexotic.shadow.components.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.shadow.components.controls.LineEvent;
import com.hexotic.shadow.components.controls.LineListener;
import com.hexotic.shadow.components.controls.LogLine;
import com.hexotic.shadow.configurations.Flags;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;
import com.hexotic.shadow.logs.Log;
import com.hexotic.shadow.logs.LogListener;

public class LogPanel extends JPanel{

	public static final int NO_PAUSE = 0;
	public static final int GROW_BUT_DONT_REMOVE =1;
	public String filter = "";
	
	private TreeMap<Integer, LogLine> lines;
	
	
	private int removeNext = 0;
	private int lineCount = 0;
	private Log log;
	private LogPanel panel;
	private int mouseDownLine = -1;
	private int mouseUpLine = -1;
	
	private int pauseState = NO_PAUSE;
	
	private List<LogPanelListener> listeners;
	
	private String activeLogId = "";
	
	public LogPanel() {
		panel = this;
		listeners = new ArrayList<LogPanelListener>();
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		lines = new TreeMap<Integer, LogLine>();
		
		bindHotkeys();
		this.setFocusable(true);
		obtainFocus();
	}
	
	public void addLogPanelListener(LogPanelListener listener){
		listeners.add(listener);
	}
	
	public void notifyListeners(int event, LogLine line){
		for(LogPanelListener listener : listeners){
			listener.lineEvent(new LogPanelEvent(event, line));
		}
	}
	
	
	public void reset() {
		pauseState = NO_PAUSE;
		mouseUpLine = -1;
		mouseDownLine = -1;
		removeNext = 0;
		lineCount = 0;
		panel.removeAll();
		lines.clear();
	}
	
	public void setLog(Log log) {
		activeLogId = log.getLogId();
		
		// reset ui any previously open logs
		reset();
		
		// Refresh the UI so that everything is repainted
		refresh();
		
		this.log = log;
		if(!log.isStarted()){
			log.addLogListener(new LogListener() {
				@Override
				public void lineAppeneded(String logId, String line, int event, String flag) {
					if(activeLogId.equals(logId)){
						addLine(line, flag);
					}
				}
			});
		}
	}
	
	public void filter(String text) {
		filter = text;
		
//		if(filter.startsWith("/")){
//			text = text.substring(1,text.length());
//			System.out.println(text);
//		} else {
		for(LogLine line : lines.values()){
			if(line.getText().contains(text)){ 
				line.setVisible(true);
			} else {
				line.setVisible(false);
			}
		}
//		}
	}
	
	public Log getLog() {
		return log;
	}
	
	private void bindHotkeys() {
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent key) {
				if(key.isControlDown() && !key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_A){
					selectAll();
				} else if(key.isControlDown() && key.getKeyCode() == KeyEvent.VK_B){
					bookMarkSelected();
				} else if(key.isControlDown() && key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_A){
					selectBookmarked();
				}  else if(key.isControlDown() && key.getKeyCode() == KeyEvent.VK_C){
					copySelected();
				} else if(key.isControlDown() && key.getKeyCode() == KeyEvent.VK_F){
					notifyListeners(LogPanelEvent.HOTKEY_FIND, null);
				} else if(key.isControlDown() && key.getKeyCode() == KeyEvent.VK_W){
					notifyListeners(LogPanelEvent.HOTKEY_CLOSE, null);
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
	

	private void copySelected() {
		StringBuilder builder = new StringBuilder();
		for(LogLine line : lines.values()){
			if(line.isSelected()){
				builder.append(line.getText()+"\n");
			}
		}
		
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(new StringSelection(builder.toString()), null);
	}
	
	private void bookMarkSelected() {
		for(LogLine line : lines.values()){
			if(line.isSelected() && line.isVisible()){
				if(line.isBookmarked()){
					notifyListeners(LogPanelEvent.LINE_UNBOOKMARKED, line);
				} else {
					notifyListeners(LogPanelEvent.LINE_BOOKMARKED, line);
				}
				line.setBookmarked(!line.isBookmarked());
			}
		}	
		revalidate();
		repaint();
	}
	
	public void selectBookmarked() {
		for(LogLine line : lines.values()){
			if(line.isVisible()){
				line.setSelected(line.isBookmarked());
			}
		}
		revalidate();
		repaint();
	}
	
	public void selectAll() {
		for(LogLine line : lines.values()){
			if(line.isVisible()){
				line.setSelected(true);
			}
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
		while(lines.size() > Constants.LOG_MAX +1){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
			      panel.remove(0);
				}
			});
			notifyListeners(LogPanelEvent.LINE_REMOVED, lines.get(removeNext));
			lines.remove(removeNext);
			removeNext++;
		}
	}
	
	public void updateLineFlags() {
		for(LogLine logLine : lines.values()){
			// Set the default background
			logLine.setBackground(Theme.LINE_BACKGROUND);
			String flag = logLine.getFlag();
			
			// update the background for anything that matches flags
			switch(flag){
			case Flags.COUNTER_ERROR:
				logLine.setLineColor(Theme.ERROR_COLOR);
				break;
			case Flags.COUNTER_WARNING:
				logLine.setLineColor(Theme.WARNING_COLOR);
				break;
			case Flags.COUNTER_SUCCESS:
				logLine.setLineColor(Theme.SUCCESS_COLOR);
				break;
			case Flags.COUNTER_INFO:
				logLine.setLineColor(Theme.INFO_COLOR);
				break;
			}
		}
	}
	
	public void addLine(String line, String flag) {
		LogLine logLine = new LogLine(lineCount, flag, line);
		lines.put(lineCount, logLine);
		
		switch(flag){
		case Flags.COUNTER_ERROR:
			logLine.setLineColor(Theme.ERROR_COLOR);
			break;
		case Flags.COUNTER_WARNING:
			logLine.setLineColor(Theme.WARNING_COLOR);
			break;
		case Flags.COUNTER_SUCCESS:
			logLine.setLineColor(Theme.SUCCESS_COLOR);
			break;
		case Flags.COUNTER_INFO:
			logLine.setLineColor(Theme.INFO_COLOR);
			break;
		}
		
		if(!filter.isEmpty() && !logLine.getText().contains(filter)){
			logLine.setVisible(false);
		}
		this.add(logLine);
		
		if(lines.size() > Constants.LOG_MAX+1 && pauseState == NO_PAUSE){
			
			// Cleanup old lines when the JVM can handle it
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
			      panel.remove(0);
				}
			});
			
			notifyListeners(LogPanelEvent.LINE_REMOVED, lines.get(removeNext));
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
		
		// notify everyone else that the line was appended
		notifyListeners(LogPanelEvent.LINE_APPENDED, logLine);
		
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
			try {
				Image img = Resources.getInstance().getImage("Default.png");
				g2d.drawImage(img, getWidth()/2-img.getWidth(null)/2, getHeight()/2-img.getHeight(null)/2, null);
			} catch (ResourceException e) {
				e.printStackTrace();
			}
		}
		
	}
	

}
