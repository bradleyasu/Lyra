package com.hexotic.lyra.components.frames;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.lyra.components.panels.LogPanel;
import com.hexotic.lyra.components.panels.SidePanel;
import com.hexotic.lyra.constants.Constants;

public class LyraFrame extends JInternalFrame{

	private LogPanel logPanel;
	private SidePanel sidePanel;
	
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
		sidePanel = new SidePanel();
		
		panel.add(logPanel, BorderLayout.CENTER);
		panel.add(sidePanel, BorderLayout.WEST);
		
		
		this.add(panel, BorderLayout.CENTER);
	}
}
