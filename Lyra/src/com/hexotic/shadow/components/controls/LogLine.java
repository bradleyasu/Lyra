package com.hexotic.shadow.components.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hexotic.lib.ui.input.textfield.ModernTextField;
import com.hexotic.shadow.constants.Theme;

public class LogLine extends JPanel{

	private int lineNumber;
	private Color lineColor = Theme.LINE_BACKGROUND;
	private String line;
	private boolean isSelected = false;
	private boolean isHovering = false;
	private boolean isEditing = false;
	private boolean isBookmarked = false;
	
	private ModernTextField editField = null;
	private List<LineListener> listeners;
	
	private String flag = "";
	
	public LogLine(int lneNumber, String flag, String line) {
		this.lineNumber = lneNumber;
		this.line = line;
		this.flag = flag;
		listeners = new ArrayList<LineListener>();
		
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(80000, 20));
		this.setMinimumSize(this.getPreferredSize());
		this.setMaximumSize(this.getPreferredSize());
		
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				notifyListeners(new LineEvent(lineNumber, LineEvent.CLICKED));
				refresh();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// If the user is holding ctrl and mouse key, auto select this line - they are probably trying to drag select
				notifyListeners(new LineEvent(lineNumber, LineEvent.ENTERED));
				isHovering = true;
				refresh();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				notifyListeners(new LineEvent(lineNumber, LineEvent.EXITED));
				isHovering = false;
				refresh();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				notifyListeners(new LineEvent(lineNumber, LineEvent.PRESSED));
				refresh();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				notifyListeners(new LineEvent(lineNumber, LineEvent.RELEASE));
				refresh();
			}
		});
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		

	}
	
	public String getFlag() {
		return flag;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public void addLineListener(LineListener listener) {
		listeners.add(listener);
	}
	
	private void notifyListeners(LineEvent event){
		for(LineListener listener: listeners){
			listener.lineAction(event);
		}
	}
	
	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}

	public void setBookmarked(boolean bookmarked){
		isBookmarked = bookmarked;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public boolean isBookmarked() {
		return isBookmarked;
	}
	
	public boolean isEditable() {
		return isEditing;
	}
	
	public void makeEdit() {
		JLabel space = new JLabel();
		space.setPreferredSize(new Dimension(Theme.LINE_NUMBER_WIDTH+1, 20));
		this.add(space, BorderLayout.WEST);
		editField = new ModernTextField(line, "");
		editField.setEditable(false);
		editField.setBackground(Theme.LINE_BACKGROUND);
		editField.setForeground(Theme.LINE_FOREGROUND);
		editField.setSelectionColor(Theme.LINE_SELECT_COLOR);
		editField.setFont(Theme.LINE_FONT.deriveFont(Theme.LINE_FONT_SIZE));
		editField.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE){
					removeEdit();
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
			
		});
		this.add(editField, BorderLayout.CENTER);
		isEditing = true;
		refresh();
	}
	
	public void removeEdit() {
		if(editField != null){
			this.remove(editField);
			editField = null;
			refresh();
		}
	}
	
	public void refresh() {
		revalidate();
		repaint();
	}
	
	public String getText() {
		return line;
	}
	
	public void setLineColor(Color color){
		lineColor = color;
	}
	
	@Override
	public void paintComponent(Graphics g){ 
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Prepare Font
		Font font = Theme.LINE_FONT.deriveFont(Theme.LINE_FONT_SIZE);
		g2d.setFont(font);
		
		FontMetrics metrics = g2d.getFontMetrics(font);
		int fht = metrics.getHeight();
		int fwd = metrics.stringWidth(String.valueOf(lineNumber));

		// Resize number pane if needed
		if(fwd+2 >= Theme.LINE_NUMBER_WIDTH) {
			Theme.getTheme().setLineNumberWidth(fwd+2);
		}
		
		
		// Draw Background
		if(isSelected){
			g2d.setColor(Theme.LINE_SELECT_COLOR);
		} else {
			g2d.setColor(lineColor);
		}
		
		if(isHovering && isSelected){
			g2d.setColor(g2d.getColor().darker());
		} else if(isHovering){
			g2d.setColor(Theme.LINE_HOVER_COLOR);
		}
		
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		if(isBookmarked){
			g2d.setColor(Theme.LINE_BOOKMARK_COLOR);
		} else {
			g2d.setColor(Theme.LINE_NUMBER_BACKGROUND);
		}
		g2d.fillRect(0, 0, Theme.LINE_NUMBER_WIDTH, getHeight());
		
		

		// Draw Line
		g2d.setColor(Theme.LINE_NUMBER_COLOR);
		g2d.drawString(String.valueOf(lineNumber), Theme.LINE_NUMBER_WIDTH-fwd-1, fht);
		
		g2d.drawLine(Theme.LINE_NUMBER_WIDTH, 0, Theme.LINE_NUMBER_WIDTH, getHeight());
		
		// Draw log line
		g2d.setColor(Theme.LINE_FOREGROUND);
		
		g2d.drawString(line, Theme.LINE_NUMBER_WIDTH + 4 ,fht);
		
	}
}
