package com.hexotic.lyra.components.frames;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.lyra.components.panels.LogPanel;
import com.hexotic.lyra.constants.Constants;

public class LyraFrame extends JInternalFrame{

	private LogPanel logPanel;
	
	public LyraFrame() {
		this.setLayout(new BorderLayout());
		buildFrame();
		
		this.setSize(Constants.WIDTH-Constants.X_OFFSET, Constants.HEIGHT-Constants.Y_OFFSET);
		this.setBorder(BorderFactory.createEmptyBorder());
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
	}
	
	private void buildFrame() {
		JPanel panel = new JPanel(new BorderLayout());
		logPanel = new LogPanel();
		
		panel.add(logPanel, BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.CENTER);
	}
}
