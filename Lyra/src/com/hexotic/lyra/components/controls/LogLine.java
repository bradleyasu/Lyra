package com.hexotic.lyra.components.controls;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.hexotic.lyra.constants.Theme;

public class LogLine extends JPanel{

	private int lineNumber;
	private String line;
	
	public LogLine(int lineNumber, String line) {
		this.lineNumber = lineNumber;
		this.line = line;
		
		this.setPreferredSize(new Dimension(8000, 20));
		this.setMinimumSize(this.getPreferredSize());
		this.setMaximumSize(this.getPreferredSize());
	}
	
	@Override
	public void paintComponent(Graphics g){ 
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw Background
		g2d.setColor(Theme.LINE_BACKGROUND);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		g2d.setColor(Theme.LINE_NUMBER_BACKGROUND);
		g2d.fillRect(0, 0, Theme.LINE_NUMBER_WIDTH, getHeight());
		
		// Draw Line Number
		Font font = Theme.LINE_FONT.deriveFont(Theme.LINE_FONT_SIZE);
		g2d.setFont(font);
		
		FontMetrics metrics = g2d.getFontMetrics(font);
		int fht = metrics.getHeight();
		int fwd = metrics.stringWidth(String.valueOf(lineNumber));
		
		g2d.setColor(Theme.LINE_NUMBER_COLOR);
		g2d.drawString(String.valueOf(lineNumber), Theme.LINE_NUMBER_WIDTH-fwd-1, fht);
		
		g2d.drawLine(Theme.LINE_NUMBER_WIDTH, 0, Theme.LINE_NUMBER_WIDTH, getHeight());
		
		// Draw log line
		g2d.setColor(Theme.LINE_FOREGROUND);
		g2d.drawString(line, Theme.LINE_NUMBER_WIDTH + 4 ,fht);
		
	}
}
