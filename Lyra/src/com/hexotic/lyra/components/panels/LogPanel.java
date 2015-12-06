package com.hexotic.lyra.components.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.hexotic.lyra.components.controls.LogLine;
import com.hexotic.lyra.constants.Theme;

public class LogPanel extends JPanel{

	private TreeMap<Integer, LogLine> lines;
	
	public LogPanel() {
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		lines = new TreeMap<Integer, LogLine>();
	}
	
	
	public void addLine(String line) {
		LogLine logLine = new LogLine(lines.size(), line);
		lines.put(lines.size(), logLine);
		this.add(logLine);
		
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
