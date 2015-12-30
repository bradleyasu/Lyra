package com.hexotic.shadow.components.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class MenuFrame extends JInternalFrame{
	
	public MenuFrame(){
		this.setSize(Constants.SIDEBAR_WIDTH, Constants.SIDEBAR_HEIGHT-Constants.Y_OFFSET-20-10);
		this.setLocation(0,Constants.FOOTER_SIZE/2);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder());
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Theme.FOOTER_BACKGROUND_DARKER);
		this.add(panel, BorderLayout.CENTER);
	}

}
