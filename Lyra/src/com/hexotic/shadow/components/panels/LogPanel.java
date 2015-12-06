package com.hexotic.shadow.components.panels;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.hexotic.shadow.components.controls.LogLine;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;
import com.hexotic.shadow.logs.Log;
import com.hexotic.shadow.logs.LogListener;

public class LogPanel extends JPanel{

	private TreeMap<Integer, LogLine> lines;
	private int removeNext = 0;
	private int lineCount = 0;
	private Log log;
	private LogPanel panel;
	
	public LogPanel() {
		panel = this;
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		lines = new TreeMap<Integer, LogLine>();
		
		bindHotkeys();
		this.setFocusable(true);
		this.requestFocus();
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
			line.setSelected(!line.isSelected());
		}
		revalidate();
		repaint();
	}
	
	public void addLine(String line) {
		LogLine logLine = new LogLine(lineCount, line);
		lines.put(lineCount, logLine);
		this.add(logLine);
		
		if(lines.size() > Constants.LOG_MAX){
			
			// Cleanup old lines when the JVM can handle it
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
			      panel.remove(0);
				}
			});
			
			lines.remove(removeNext);
			removeNext++;
		}
		
		lineCount++;
		
		revalidate();
		repaint();
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
