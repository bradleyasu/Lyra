package com.hexotic.shadow.components.controls;

import java.awt.BorderLayout;
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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.Highlighter;

import com.hexotic.lib.ui.input.textfield.ModernTextField;
import com.hexotic.shadow.constants.Theme;

public class LogLine extends JPanel{

	private int lineNumber;
	private String line;
	private boolean isSelected = false;
	private boolean isHovering = false;
	private ModernTextField editField = null;
	
	public LogLine(int lineNumber, String line) {
		this.lineNumber = lineNumber;
		this.line = line;
		
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(8000, 20));
		this.setMinimumSize(this.getPreferredSize());
		this.setMaximumSize(this.getPreferredSize());
		
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isSelected = !isSelected;
				if (e.getClickCount() == 2 && !e.isConsumed()) {
				     e.consume();
				     if(editField == null){
				    	 makeEdit();
				     }
				}
				refresh();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// If the user is holding ctrl and mouse key, auto select this line - they are probably trying to drag select
				int buttonsDownMask = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON2_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK; 
				if ((e.getModifiersEx() & buttonsDownMask) != 0  && e.isControlDown()){
					isSelected = !isSelected;
				} else {
					isHovering = true;
				}
				refresh();
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				isHovering = false;
				refresh();
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		

	}
	
	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	private void makeEdit() {
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
		refresh();
	}
	
	private void removeEdit() {
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
		} else if(isHovering) {
			g2d.setColor(Theme.LINE_HOVER_COLOR);
		} else {
			g2d.setColor(Theme.LINE_BACKGROUND);
		}
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		g2d.setColor(Theme.LINE_NUMBER_BACKGROUND);
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
