package com.hexotic.shadow.components.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.lib.ui.input.textfield.ModernTextField;
import com.hexotic.shadow.configurations.Flags;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class FlagConfigurationFrame extends JInternalFrame{

	private FlagConfigPanel innerPanel;
	private Color backgroundColor = Theme.DIALOG_COLOR;
	
	private ModernTextField configRegex;
	private String selectedLogId = "";
	private String flagType = "";
	
	public FlagConfigurationFrame() {
		this.setSize(Constants.FLAG_CONFIG_WIDTH, Constants.FLAG_CONFIG_HEIGHT);
		this.setLocation(Constants.FOOTER_COUNTER_WIDTH*2, Constants.HEIGHT - Constants.FOOTER_SIZE - Constants.FLAG_CONFIG_HEIGHT);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder());
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
		innerPanel = new FlagConfigPanel();
		this.add(innerPanel, BorderLayout.CENTER);
		
	}
	
	public void setFlagToConfigure(String logId, String flag) {
		this.selectedLogId = logId;
		this.flagType = flag;
		configRegex.setText(Flags.getInstance().getLogFlags(logId).get(flag));
		checkInput();
		switch(flag){
		case Flags.COUNTER_SUCCESS:
			backgroundColor = Theme.SUCCESS_COLOR;
			break;
		case Flags.COUNTER_INFO:
			backgroundColor = Theme.INFO_COLOR;
			break;
		case Flags.COUNTER_WARNING:
			backgroundColor = Theme.WARNING_COLOR;
			break;
		case Flags.COUNTER_ERROR:
			backgroundColor = Theme.ERROR_COLOR;
			break;
		case Flags.COUNTER_BOOKMARK:
			backgroundColor = Theme.LINE_BOOKMARK_COLOR;
			break;
		}
		configRegex.requestFocus();
		configRegex.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				checkInput();
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					innerPanel.requestFocus();
				} else {
					Flags.getInstance().setFlag(selectedLogId, flagType, configRegex.getText());
				}
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});
		revalidate();
		repaint();
	}
	
	private void checkInput() {
		boolean isRegex;
		try {
		  Pattern.compile(configRegex.getText());
		  isRegex = true;
		} catch (PatternSyntaxException e) {
		  isRegex = false;
		}
		configRegex.setAccepted(isRegex);
	}
	
	private FlagConfigurationFrame getThis() {
		return this;
	}
	
	public class FlagConfigPanel extends JPanel {
		
		public FlagConfigPanel() {
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			JLabel field = new JLabel("Flag Lines This Color That Contain:");
			configRegex = new ModernTextField("", "");
			configRegex.setPreferredSize(new Dimension(Constants.FLAG_CONFIG_WIDTH-20, 25));
			
			field.setFont(Theme.FOOTER_FONT.deriveFont((float)14.0).deriveFont(Font.BOLD));
			field.setForeground(Theme.FOOTER_BACKGROUND_DARKER);
			configRegex.setFont(Theme.FOOTER_FONT.deriveFont((float)12.0).deriveFont(Font.BOLD));
			configRegex.setSelectionColor(Theme.LINE_SELECT_COLOR);
			this.add(field);
			this.add(configRegex);
			
			configRegex.addFocusListener(new FocusListener(){
				@Override
				public void focusGained(FocusEvent arg0) {
				}
				@Override
				public void focusLost(FocusEvent arg0) {
					getThis().setVisible(false);
					Flags.getInstance().storeProperties();
				}
			});
		}
		
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g2d.setColor(backgroundColor);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			
		}
	}
	
}
